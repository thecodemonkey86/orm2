package cpp.entity.method;

import util.StringUtil;

import java.util.ArrayList;

import codegen.CodeUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.QString;
import cpp.core.expression.Var;
import cpp.entity.EntityCls;
import cpp.entityrepository.ClsEntityRepository;
import cpp.lib.ClsQVariantList;
import cpp.lib.ClsQList;
import cpp.lib.ClsSql;
import cpp.orm.OrmUtil;
import database.column.Column;
import database.relation.ManyRelation;

public class MethodRemoveAllManyRelatedEntities extends Method {

	protected ManyRelation rel;
	
	public static String getMethodName(ManyRelation r) {
		return "removeAll"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrName(r));
	}
	
	public MethodRemoveAllManyRelatedEntities(ManyRelation r) {
		super(Public, Types.Void, getMethodName(r));
		rel=r;
	}

	@Override
	public void addImplementation() {
		EntityCls parent = (EntityCls) this.parent;
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		addInstr(a.callMethod(ClsQList.clear).asInstruction());
		
		ArrayList<String> columns = new ArrayList<>();
		Var varParams = _declare(Types.QVariantList, "params");
		
		for(int i=0;i<rel.getSourceColumnCount();i++) {
			columns.add(rel.getSourceMappingColumn(i).getEscapedName()+"=?");
		}
		
		for(Column colPk : rel.getSourceTable().getPrimaryKey()) {
			_callMethodInstr(varParams, ClsQVariantList.append,parent.accessThisAttrGetterByColumn(colPk));
		}
		
		String sql = String.format("delete from %s where %s", rel.getMappingTable().getEscapedName(), CodeUtil.commaSep(columns));
		
		addInstr(Types.Sql.callStaticMethod(ClsSql.execute, _this().accessAttr(EntityCls.repository).callAttrGetter(ClsEntityRepository.sqlCon),QString.fromStringConstant(sql),varParams).asInstruction());
		
		
		
		/*Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		ForeachLoop foreach = _foreach(new Var(((ManyAttr)a).getElementType().toConstRef(), "_relationBean"), a);
		foreach.addInstr(_this().callMethodInstruction(MethodRemoveManyToManyRelatedEntity.getMethodName(rel), foreach.getVar()));*/
		
//		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
//		EntityCls relationBean = Entities.get( rel.getDestTable());
//		Var varForeach = new Var(((ManyAttr)a).getElementType().toConstRef(), "_relationBean");
//		if (relationBean.getTbl().getPrimaryKey().isMultiColumn()) {
//			ForeachLoop foreachRelationBeans = _foreach(varForeach, a);	
//			Struct pkType=relationBean.getStructPk();
//			Var idRemoved = foreachRelationBeans._declare(pkType, "idRemoved");
//			for(Column col:relationBean.getTbl().getPrimaryKey().getColumns()) {
//				
//				foreachRelationBeans._assign(idRemoved.accessAttr(col
//						.getCamelCaseName()), varForeach
//						.callAttrGetter(
//								col
//								.getCamelCaseName()
//						));
//			}
//			
//			
//			
//			/*foreachRelationBeans.addInstr(
//					parent.getAttrByName(
//							a.getName()+"Removed")
//							.callMethod("append",
//									idRemoved
//								).asInstruction());	*/
//		} else {
//			ForeachLoop foreachRelationBeans = _foreach(varForeach, a);	
//			
//			/*foreachRelationBeans.addInstr(
//				parent.getAttrByName(
//						a.getName()+"Removed")
//						.callMethod("append",
//								varForeach.callMethod("get" + relationBean.getTbl().getPrimaryKey().getFirstColumn().getUc1stCamelCaseName())
//																
//							).asInstruction());	*/
//			
//		}
//		
//
//		_callMethodInstr(a, ClsQVector.clear);
	}

}
