package php.cls.bean.method;

import php.Types;
import php.cls.Attr;
import php.cls.PhpCls;
import php.cls.Method;
import php.cls.Param;
import php.cls.Type;
import php.cls.bean.BeanCls;
import php.cls.bean.Beans;
import php.cls.expression.Expression;
import php.cls.expression.NewOperator;
import php.cls.expression.Var;

import php.orm.OrmUtil;
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
		/*PhpCls parent = (PhpCls) this.parent;
		
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		_if(a.isNull()).addIfInstr(a.assign(new NewOperator(a.getType())));
		Param pBean = getParam("bean");
		addInstr(a.arrayPush(pBean));
		BeanCls relationBean = Beans.get( rel.getDestTable());
		Attr aAdded = parent.getAttrByName(
				a.getName()+"Added");
		_if(aAdded.isNull()).addIfInstr(aAdded.assign(new NewOperator(aAdded.getType())));
		if (relationBean.getTbl().getPrimaryKey().isMultiColumn()) {
			
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
					aAdded.arrayPush(
							idAdded));	
				
		} else {
			
						
			addInstr(
					
					aAdded.arrayPush(
									pBean
									.callAttrGetter(
											relationBean.getTbl().getPrimaryKey().getFirstColumn()
											.getCamelCaseName()
									)
								));	
		}
		
		
		
		
*/
	}

}
