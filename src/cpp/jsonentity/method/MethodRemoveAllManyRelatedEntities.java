package cpp.jsonentity.method;

import util.StringUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Struct;
import cpp.core.expression.Var;
import cpp.core.instruction.ForeachLoop;
import cpp.jsonentity.JsonEntities;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentity.ManyAttr;
import cpp.lib.ClsQList;
import cpp.orm.OrmUtil;
import database.column.Column;
import database.relation.IManyRelation;

public class MethodRemoveAllManyRelatedEntities extends Method {

	protected IManyRelation rel;
	
	public static String getMethodName(IManyRelation r) {
		return "removeAll"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrName(r));
	}
	
	public MethodRemoveAllManyRelatedEntities(IManyRelation r) {
		super(Public, Types.Void, getMethodName(r));
		rel=r;
	}

	@Override
	public void addImplementation() {
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		JsonEntity relationEntity = JsonEntities.get( rel.getDestTable());
		Var varForeach = new Var(((ManyAttr)a).getElementType().toConstRef(), "_relationEntity");
		if (relationEntity.getTbl().getPrimaryKey().isMultiColumn()) {
			ForeachLoop foreachRelationEntities = _foreach(varForeach, a);	
			Struct pkType=relationEntity.getStructPk();
			Var idRemoved = foreachRelationEntities._declare(pkType, "idRemoved");
			for(Column col:relationEntity.getTbl().getPrimaryKey().getColumns()) {
				
				foreachRelationEntities._assign(idRemoved.accessAttr(col
						.getCamelCaseName()), varForeach
						.callAttrGetter(
								col
								.getCamelCaseName()
						));
			}
			foreachRelationEntities.addInstr(
					parent.getAttrByName(
							a.getName()+"Removed")
							.callMethod("append",
									idRemoved
								).asInstruction());	
		} else {
			ForeachLoop foreachRelationEntities = _foreach(varForeach, a);	
			
			foreachRelationEntities.addInstr(
				parent.getAttrByName(
						a.getName()+"Removed")
						.callMethod("append",
								varForeach.callMethod("get" + relationEntity.getTbl().getPrimaryKey().getFirstColumn().getUc1stCamelCaseName())
																
							).asInstruction());	
			
		}
		

		_callMethodInstr(a, ClsQList.clear);
	}

}
