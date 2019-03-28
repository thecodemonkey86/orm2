package sunjava.bean.method;

import database.column.Column;
import database.relation.ManyRelation;
import sunjava.bean.BeanCls;
import sunjava.bean.Beans;
import sunjava.core.Attr;
import sunjava.core.JavaCls;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Type;
import sunjava.core.Types;
import sunjava.core.expression.Expression;
import sunjava.core.expression.NewOperator;
import sunjava.core.expression.Var;
import sunjava.lib.ClsArrayList;
import sunjava.orm.OrmUtil;

public class MethodAddManyToManyRelatedBean extends Method {

	protected ManyRelation rel;
	
	public MethodAddManyToManyRelatedBean(ManyRelation r, Param p) {
		super(Public, Types.Void, OrmUtil.getAddRelatedBeanMethodName(r));
		addParam(p);
		rel=r;
	}

	@Override
	public void addImplementation() {
		JavaCls parent = (JavaCls) this.parent;
		
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		_if(a.isNull()).addIfInstr(a.assign(new NewOperator(a.getType())));
		Param pBean = getParam("bean");
		addInstr(a.callMethod(ClsArrayList.add,pBean).asInstruction());
		BeanCls relationBean = Beans.get( rel.getDestTable());
		Attr aAdded = parent.getAttrByName(
				a.getName()+"Added");
		_if(aAdded.isNull()).addIfInstr(aAdded.assign(new NewOperator(aAdded.getType())));
		if (relationBean.getTbl().getPrimaryKey().isMultiColumn()) {
			Type pkType=relationBean.getPkType();
			
			Expression[] idAddedArgs = new Expression[relationBean.getTbl().getPrimaryKey().getColumnCount()];
			
			for(int i = 0; i < idAddedArgs.length; i++) {
				idAddedArgs[i] = pBean.callAttrGetter(relationBean.getTbl().getPrimaryKey().getColumn(i).getCamelCaseName());
			}
			
			Var idAdded = _declareNew(pkType, "idAdded", idAddedArgs);
			for(Column col:relationBean.getTbl().getPrimaryKey().getColumns()) {
				addInstr(idAdded.callAttrSetterMethodInstr(col
						.getCamelCaseName(), pBean
						.callAttrGetter(
								col
								.getCamelCaseName()
						)));
			}
			
			addInstr(
					aAdded.callMethod(ClsArrayList.add,
							idAdded).asInstruction());	
				
		} else {
			
						
			addInstr(
					
					aAdded.callMethod(ClsArrayList.add,
									pBean
									.callAttrGetter(
											relationBean.getTbl().getPrimaryKey().getFirstColumn()
											.getCamelCaseName()
									)
								).asInstruction());	
		}
		
		
		
		

	}

}
