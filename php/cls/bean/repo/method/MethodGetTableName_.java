package php.cls.bean.repo.method;
//package php.cls.bean.repo.method;
//
//import php.Types;
//import php.cls.Method;
//import php.cls.bean.BeanCls;
//import php.cls.expression.PhpStringLiteral;
//
//
//public class MethodGetTableName extends Method{
//	protected BeanCls bean;
//	
//	public MethodGetTableName(BeanCls bean) {
//		super(Method.Public, Types.String, getMethodName(bean));
//		setStatic(true);
//		this.bean = bean;
//	}
//
//	@Override
//	public void addImplementation() {
//		_return(new PhpStringLiteral(BeanCls.getDatabase().getEscapedTableName(bean.getTbl())));
//		
//	}
//	
//	public static String getMethodName(BeanCls bean) {
//		return "getTableName"+ bean.getName();
//	}
//
//}
