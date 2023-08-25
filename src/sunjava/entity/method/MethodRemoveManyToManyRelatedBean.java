package sunjava.entity.method;

import database.column.Column;
import database.relation.ManyRelation;
import sunjava.core.Attr;
import sunjava.core.JavaCls;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Type;
import sunjava.core.Types;
import sunjava.core.expression.Expression;
import sunjava.core.expression.NewOperator;
import sunjava.core.expression.Var;
import sunjava.entity.Entities;
import sunjava.entity.EntityCls;
import sunjava.lib.ClsArrayList;
import sunjava.orm.OrmUtil;
import util.StringUtil;

public class MethodRemoveManyToManyRelatedBean extends Method {

	protected ManyRelation rel;
	
	public MethodRemoveManyToManyRelatedBean(ManyRelation r, Param p) {
		super(Public, Types.Void, "remove"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r)));
		addParam(p);
		rel=r;
	}

	@Override
	public void addImplementation() {
		JavaCls parent = (JavaCls) this.parent;
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		addInstr(a.callMethod(ClsArrayList.remove,getParam("entity")).asInstruction());
		EntityCls relationBean = Entities.get( rel.getDestTable());
		Param pBean = getParam("entity");
		Attr aRemoved = parent.getAttrByName(
				a.getName()+"Removed");
		_if(aRemoved.isNull()).addIfInstr(aRemoved.assign(new NewOperator(aRemoved.getType())));
		
		if (relationBean.getTbl().getPrimaryKey().isMultiColumn()) {
			Type pkType=relationBean.getPkType();
Expression[] idAddedArgs = new Expression[relationBean.getTbl().getPrimaryKey().getColumnCount()];
			
			for(int i = 0; i < idAddedArgs.length; i++) {
				idAddedArgs[i] = pBean.callAttrGetter(relationBean.getTbl().getPrimaryKey().getColumn(i).getCamelCaseName());
			}
			Var idRemoved = _declareNew(pkType, "idRemoved", idAddedArgs);
			for(Column col:relationBean.getTbl().getPrimaryKey().getColumns()) {
				addInstr( idRemoved.callAttrSetterMethodInstr(col
						.getCamelCaseName(), getParam("entity")
						.callAttrGetter(
								col
								.getCamelCaseName()
						)));
			}
			addInstr(
					aRemoved
							.callMethod(ClsArrayList.add,
									idRemoved
								).asInstruction());	
				
		} else {
			addInstr(
					aRemoved
							.callMethod(ClsArrayList.add,
									getParam("entity")
									.callAttrGetter(
											relationBean.getTbl().getPrimaryKey().getFirstColumn()
											.getCamelCaseName()
									)
								).asInstruction());	
		}
	}

}
