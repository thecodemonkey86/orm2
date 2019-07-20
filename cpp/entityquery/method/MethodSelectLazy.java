package cpp.entityquery.method;
//package cpp.beanquery.method;
//
//import cpp.bean.BeanCls;
//import cpp.bean.method.MethodGetSelectFields;
//import cpp.bean.method.MethodGetTableName;
//import cpp.beanquery.ClsBeanQuerySelect;
//import cpp.core.Cls;
//import cpp.core.Method;
//import cpp.core.QString;
//import cpp.core.expression.BoolExpression;
//import cpp.core.expression.Expression;
//
//public class MethodSelectLazy extends Method{
//
//	BeanCls bean;
//	public MethodSelectLazy(BeanCls bean,Cls parentType) {
//		super(Public, parentType.toRef(), "selectLazy");
//		this.bean = bean;
//	}
//
//	@Override
//	public void addImplementation() {
//		Expression attrMainBeanAlias = _this().accessAttr("mainBeanAlias");
//		
//		_assign(attrMainBeanAlias, QString.fromStringConstant("e1"));
//		_assign(_this().accessAttr("selectFields"), this.bean.callStaticMethod(MethodGetSelectFields.getMethodName(), attrMainBeanAlias ));
//		_assign(_this().accessAttr(ClsBeanQuerySelect.table), this.bean.callStaticMethod(MethodGetTableName.getMethodName(), attrMainBeanAlias ));
//		_assign(_this().accessAttr("lazyLoading"), BoolExpression.TRUE);
//		_return(_this().dereference());
//	}
//
//}
