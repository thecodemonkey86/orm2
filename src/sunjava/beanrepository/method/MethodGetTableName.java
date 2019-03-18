package sunjava.beanrepository.method;

import sunjava.bean.BeanCls;
import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Types;

public class MethodGetTableName extends Method{
	protected BeanCls bean;
	
	public MethodGetTableName(BeanCls bean) {
		super(Method.Public, Types.String, getMethodName(bean));
		setStatic(true);
		this.bean = bean;
	}

	@Override
	public void addImplementation() {
		_return(JavaString.stringConstant(BeanCls.getDatabase().getEscapedTableName(bean.getTbl())));
		
	}
	
	public static String getMethodName(BeanCls bean) {
		return "getTableName"+ bean.getName();
	}

}
