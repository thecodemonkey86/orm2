package php.bean.method;

import database.column.Column;
import database.relation.ManyRelation;
import php.bean.BeanCls;
import php.bean.Beans;
import php.core.Attr;
import php.core.Param;
import php.core.PhpCls;
import php.core.Type;
import php.core.Types;
import php.core.expression.Expression;
import php.core.expression.NewOperator;
import php.core.expression.Var;
import php.core.method.Method;
import php.orm.OrmUtil;

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
