package sunjava.entityrepository.method;

import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Types;
import sunjava.entity.EntityCls;

public class MethodGetTableName extends Method{
	protected EntityCls bean;
	
	public MethodGetTableName(EntityCls bean) {
		super(Method.Public, Types.String, getMethodName(bean));
		setStatic(true);
		this.bean = bean;
	}

	@Override
	public void addImplementation() {
		_return(JavaString.stringConstant(EntityCls.getDatabase().getEscapedTableName(bean.getTbl())));
		
	}
	
	public static String getMethodName(EntityCls bean) {
		return "getTableName"+ bean.getName();
	}

}
