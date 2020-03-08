package cpp.entity.method;

import util.StringUtil;

import java.util.ArrayList;

import codegen.CodeUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.Var;
import cpp.entity.EntityCls;
import cpp.entity.ManyAttr;
import cpp.entityrepository.ClsEntityRepository;
import cpp.lib.ClsQVariantList;
import cpp.lib.ClsQVector;
import cpp.lib.ClsSql;
import cpp.orm.OrmUtil;
import database.column.Column;
import database.relation.ManyRelation;

public class MethodRemoveManyToManyRelatedEntity extends Method {

	protected ManyRelation rel;
	Param pBean;
	
	public static String getMethodName(ManyRelation r) {
		return  "remove"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r));
	}
	
	public MethodRemoveManyToManyRelatedEntity(ManyRelation r) {
		super(Public, Types.Void,getMethodName(r));
		pBean = addParam(new ManyAttr(r).getElementType().toConstRef(),"entity");
		rel=r;
	}

	@Override
	public void addImplementation() {
		EntityCls parent = (EntityCls) this.parent;
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		addInstr(a.callMethod(ClsQVector.removeOne,pBean).asInstruction());
		
		ArrayList<String> columns = new ArrayList<>();
		Var varParams = _declare(Types.QVariantList, "params");
		
		for(int i=0;i<rel.getSourceColumnCount();i++) {
			columns.add(rel.getSourceMappingColumn(i).getEscapedName()+"=?");
		}
		for(int i=0;i<rel.getDestColumnCount();i++) {
			columns.add(rel.getDestMappingColumn(i).getEscapedName()+"=?");
		}
		
		for(Column colPk : rel.getSourceTable().getPrimaryKey()) {
			_callMethodInstr(varParams, ClsQVariantList.append,parent.accessThisAttrGetterByColumn(colPk));
		}
		for(Column colPk : rel.getDestTable().getPrimaryKey()) {
			_callMethodInstr(varParams, ClsQVariantList.append, pBean.callAttrGetter(colPk.getCamelCaseName()));
		}
		
		String sql = String.format("delete from %s where %s", rel.getMappingTable().getEscapedName(), CodeUtil.commaSep(columns));
		
		addInstr(Types.Sql.callStaticMethod(ClsSql.execute, _this().accessAttr(EntityCls.repository).callAttrGetter(ClsEntityRepository.sqlCon),QString.fromStringConstant(sql),varParams).asInstruction());
		
		
		/*EntityCls relationBean = Entities.get( rel.getDestTable());
		
		if (relationBean.getTbl().getPrimaryKey().isMultiColumn()) {
			Struct pkType=relationBean.getStructPk();
			Var idRemoved = _declare(pkType, "idRemoved");
			for(Column col:relationBean.getTbl().getPrimaryKey().getColumns()) {
				_assign(idRemoved.accessAttr(col
						.getCamelCaseName()), getParam("entity")
						.callAttrGetter(
								col
								.getCamelCaseName()
						));
			}
			addInstr(
					parent.getAttrByName(
							a.getName()+"Removed")
							.callMethod("append",
									idRemoved
								).asInstruction());	
				
		} else {
			addInstr(
					parent.getAttrByName(
							a.getName()+"Removed")
							.callMethod("append",
									getParam("entity")
									.callAttrGetter(
											relationBean.getTbl().getPrimaryKey().getFirstColumn()
											.getCamelCaseName()
									)
								).asInstruction());	
		}*/
	}

}
