package sunjava.beanrepository.query.method;

import sunjava.bean.BeanCls;
import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Types;

public class MethodGetTableName extends Method {
	BeanCls cls;
	
	public MethodGetTableName(BeanCls cls) {
		super(Public, Types.String, "getTableName");
		this.cls = cls;
	}

	@Override
	public void addImplementation() {
		_return(JavaString.stringConstant(cls.getTbl().getName()));
		
	}

}
