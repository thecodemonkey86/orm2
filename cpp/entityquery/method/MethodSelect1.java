package cpp.entityquery.method;
//package cpp.beanquery.method;
//
//import cpp.bean.BeanCls;
//import cpp.bean.method.MethodAddRelatedTableJoins;
//import cpp.bean.method.MethodGetAllSelectFields;
//import cpp.bean.method.MethodGetTableName;
//import cpp.beanquery.ClsBeanQuerySelect;
//import cpp.core.Cls;
//import cpp.core.Method;
//import cpp.core.QString;
//import cpp.core.expression.Expression;
//import cpp.core.expression.StaticMethodCall;
//
//public class MethodSelect1 extends Method{
//
//	BeanCls bean;
//	public MethodSelect1(BeanCls bean,Cls parentType) {
//		super(Public, parentType.toRef(), "select");
//		this.bean = bean;
//	}
//
//	@Override
//	public void addImplementation() {
//		Expression attrMainBeanAlias = _this().accessAttr("mainBeanAlias");
//		
//		_assign(attrMainBeanAlias, QString.fromStringConstant("b1"));
//		_assign(_this().accessAttr("selectFields"), this.bean.callStaticMethod(MethodGetAllSelectFields.getMethodName(), attrMainBeanAlias ));
//		_assign(_this().accessAttr(ClsBeanQuerySelect.table), this.bean.callStaticMethod(MethodGetTableName.getMethodName(), attrMainBeanAlias ));
//		
//		if(this.bean.hasRelations()) {
//			addInstr(new StaticMethodCall(MethodSelect1.this.bean, MethodSelect1.this.bean.getMethod(MethodAddRelatedTableJoins.getMethodName()), _this().dereference()).asInstruction());
//		}
//		_return(_this().dereference());
//	}
//
//}
