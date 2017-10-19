package sunjava.cls.bean.repo.method;

import sunjava.Types;
import sunjava.cls.JavaString;
import sunjava.cls.Method;
import sunjava.cls.bean.BeanCls;

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
