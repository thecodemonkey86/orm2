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
		/*PhpCls parent = (PhpCls) this.parent;
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		addInstr(a.arrayUnset(getParam("bean")));
		BeanCls relationBean = Beans.get( rel.getDestTable());
		Param pBean = getParam("bean");
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
						.getCamelCaseName(), getParam("bean")
						.callAttrGetter(
								col
								.getCamelCaseName()
						)));
			}
			addInstr(
					aRemoved
							.arrayPush(
									idRemoved
								));	
				
		} else {
			addInstr(
					aRemoved
							.arrayPush(
									getParam("bean")
									.callAttrGetter(
											relationBean.getTbl().getPrimaryKey().getFirstColumn()
											.getCamelCaseName()
									)
								));	
		}*/
	}

}
