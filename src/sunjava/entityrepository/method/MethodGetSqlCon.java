package sunjava.entityrepository.method;

import sunjava.core.Method;
import sunjava.core.Type;

public class MethodGetSqlCon extends Method {

	public MethodGetSqlCon(String visibility, Type returnType, String name) {
		super(visibility, returnType, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addImplementation() {
		// TODO Auto-generated method stub
		
	}

//	public MethodGetSqlCon() {
//		super(Public, Types.Connection,getMethodName() );
//		setStatic(true);
//	}
//
//	@Override
//	public void addImplementation() {
//		_return(((JavaCls) parent).accessStaticAttributeByName("sqlConnection").callMethod(ClsThreadLocal.get));
//		
//	}
//
//	public static String getMethodName() {
//		return "getSqlConnection";
//	}

}
