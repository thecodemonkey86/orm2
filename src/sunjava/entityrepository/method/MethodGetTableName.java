package sunjava.entityrepository.method;

import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Types;
import sunjava.entity.EntityCls;

public class MethodGetTableName extends Method{
	protected EntityCls entity;
	
	public MethodGetTableName(EntityCls entity) {
		super(Method.Public, Types.String, getMethodName(entity));
		setStatic(true);
		this.entity = entity;
	}

	@Override
	public void addImplementation() {
		_return(JavaString.stringConstant(EntityCls.getDatabase().getEscapedTableName(entity.getTbl())));
		
	}
	
	public static String getMethodName(EntityCls entity) {
		return "getTableName"+ entity.getName();
	}

}
