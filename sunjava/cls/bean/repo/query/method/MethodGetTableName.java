package sunjava.cls.bean.repo.query.method;

import sunjava.Types;
import sunjava.cls.JavaString;
import sunjava.cls.Method;
import sunjava.cls.bean.BeanCls;

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
