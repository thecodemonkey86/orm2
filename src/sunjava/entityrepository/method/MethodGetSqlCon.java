package sunjava.entityrepository.method;

import sunjava.core.JavaCls;
import sunjava.core.Method;
import sunjava.core.Types;
import sunjava.lib.ClsThreadLocal;

public class MethodGetSqlCon extends Method {

	public MethodGetSqlCon() {
		super(Public, Types.Connection,getMethodName() );
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		_return(((JavaCls) parent).accessStaticAttributeByName("sqlConnection").callMethod(ClsThreadLocal.get));
		
	}

	public static String getMethodName() {
		return "getSqlConnection";
	}

}
