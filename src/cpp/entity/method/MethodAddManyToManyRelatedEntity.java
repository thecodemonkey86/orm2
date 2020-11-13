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
import cpp.lib.ClsQVariantList;
import cpp.lib.ClsQVector;
import cpp.lib.ClsSql;
import cpp.orm.OrmUtil;
import cpp.util.ClsDbPool;
import database.relation.ManyRelation;

public class MethodAddManyToManyRelatedEntity extends Method {

	protected ManyRelation rel;
	Param pBean;
	public MethodAddManyToManyRelatedEntity(ManyRelation r, Param p) {
		super(Public, Types.Void, getMethodName(r));
		pBean = addParam(p);
		rel=r;
	}
	
	public static String getMethodName(ManyRelation r) {
		return "add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r));
	}

	@Override
	public void addImplementation() {
		EntityCls parent = (EntityCls) this.parent;
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		addInstr(a.callMethod(ClsQVector.append,pBean).asInstruction());
		//EntityCls relationBean = Entities.get( rel.getDestTable());
		ArrayList<String> placeholders = new ArrayList<>();
		ArrayList<String> columns = new ArrayList<>();
		Var varParams = _declare(Types.QVariantList, "params");
		
		for(int i=0;i<rel.getSourceColumnCount();i++) {
			columns.add(rel.getSourceMappingColumn(i).getEscapedName());
			_callMethodInstr(varParams, ClsQVariantList.append,parent.accessThisAttrGetterByColumn(rel.getSourceEntityColumn(i)));
		}
		for(int i=0;i<rel.getDestColumnCount();i++) {
			columns.add(rel.getDestMappingColumn(i).getEscapedName());
			_callMethodInstr(varParams, ClsQVariantList.append, pBean.callAttrGetter(rel.getDestEntityColumn(i).getCamelCaseName()));
		}
		
		for(int i=0;i<rel.getSourceTable().getPrimaryKey().getColumnCount();i++) {
			placeholders.add("?");
			
		}
		for(int i=0;i< rel.getDestTable().getPrimaryKey().getColumnCount();i++) {
			placeholders.add("?");
			
		}
		
		String sql = String.format("insert into %s (%s) values (%s)",rel.getMappingTable().getEscapedName(), CodeUtil.commaSep(columns), CodeUtil.commaSep(placeholders));
		
		addInstr(Types.Sql.callStaticMethod(ClsSql.execute, ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase),QString.fromStringConstant(sql),varParams).asInstruction());
		
		/*if (relationBean.getTbl().getPrimaryKey().isMultiColumn()) {
			Struct pkType=relationBean.getStructPk();
			Var idAdded = _declare(pkType, "idAdded");
			for(Column col:relationBean.getTbl().getPrimaryKey().getColumns()) {
				_assign(idAdded.accessAttr(col
						.getCamelCaseName()), pBean
						.callAttrGetter(
								col
								.getCamelCaseName()
						));
			}
			addInstr(
					parent.getAttrByName(
							a.getName()+"Added")
							.callMethod(ClsQVector.append,
									idAdded
								).asInstruction());	
				
		} else {
			addInstr(
					parent.getAttrByName(
							a.getName()+"Added")
							.callMethod(ClsQVector.append,
									pBean
									.callAttrGetter(
											relationBean.getTbl().getPrimaryKey().getFirstColumn()
											.getCamelCaseName()
									)
								).asInstruction());	
		}*/
		
		
		
		

	}

}
