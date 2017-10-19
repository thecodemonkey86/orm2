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
import util.StringUtil;
import model.Column;
import model.ManyRelation;

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
