package sunjava.entityrepository.query.method;

import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Types;
import sunjava.entity.EntityCls;

public class MethodGetTableName extends Method {
	EntityCls cls;
	
	public MethodGetTableName(EntityCls cls) {
		super(Public, Types.String, "getTableName");
		this.cls = cls;
	}

	@Override
	public void addImplementation() {
		_return(JavaString.stringConstant(cls.getTbl().getName()));
		
	}

}
