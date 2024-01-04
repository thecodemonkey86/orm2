package cpp.jsonentity.method;

import util.StringUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.Struct;
import cpp.core.expression.Var;
import cpp.jsonentity.JsonEntities;
import cpp.jsonentity.JsonEntity;
import cpp.lib.ClsQList;
import cpp.orm.OrmUtil;
import database.column.Column;
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
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		addInstr(a.callMethod(ClsQList.append,pBean).asInstruction());
		JsonEntity relationBean = JsonEntities.get( rel.getDestTable());
		
		if (relationBean.getTbl().getPrimaryKey().isMultiColumn()) {
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
							.callMethod(ClsQList.append,
									idAdded
								).asInstruction());	
				
		} else {
			addInstr(
					parent.getAttrByName(
							a.getName()+"Added")
							.callMethod(ClsQList.append,
									pBean
									.callAttrGetter(
											relationBean.getTbl().getPrimaryKey().getFirstColumn()
											.getCamelCaseName()
									)
								).asInstruction());	
		}
		
		
		
		

	}

}
