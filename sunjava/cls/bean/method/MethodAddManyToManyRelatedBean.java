package sunjava.cls.bean.method;

import sunjava.Types;
import sunjava.cls.Attr;
import sunjava.cls.JavaCls;
import sunjava.cls.Method;
import sunjava.cls.Param;
import sunjava.cls.Type;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.bean.Beans;
import sunjava.cls.expression.Expression;
import sunjava.cls.expression.NewOperator;
import sunjava.cls.expression.Var;
import sunjava.lib.ClsArrayList;
import sunjava.orm.OrmUtil;
import model.Column;
import model.ManyRelation;

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
