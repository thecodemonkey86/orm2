package sunjava.lib;

import sunjava.cls.JavaCls;
import sunjava.cls.Type;

public class ClsSqlParam extends JavaCls{

	public static final String get = "get";
	public static final String METHOD_NAME_GET_NULL_STRING = "getNullString";
	public static final String METHOD_NAME_GET_NULL_INTEGER = "getNullInteger";
	public static final String METHOD_NAME_GET_NULL_SHORT = "getNullShort";
	public static final String METHOD_NAME_GET_NULL_DOUBLE = "getNullDouble";
	public static final String METHOD_NAME_GET_NULL_FLOAT = "getNullFloat";
	public static final String METHOD_NAME_GET_NULL_LONG = "getNullLong";
	public static final String METHOD_NAME_GET_NULL_ZONEDDATETIME = "getNullZonedDateTime";
	public static final String METHOD_NAME_GET_NULL_LOCALDATETIME = "getNullLocalDateTime";
	public static final String METHOD_NAME_GET_NULL_LOCALDATE = "getNullLocalDate";
	public static final String METHOD_NAME_GET_NULL_BYTE = "getNullByte";
	public static final String METHOD_NAME_GET_NULL_BOOLEAN = "getNullBoolean";
	public static final String METHOD_NAME_GET_NULL_BYTES = "getNullBytes";


	
	public ClsSqlParam() {
		super("SqlParam", "sql");
		
		addMethod(new StaticLibMethod(this, get));
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_NULL_STRING)); 
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_NULL_INTEGER)); 
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_NULL_SHORT));
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_NULL_DOUBLE));
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_NULL_FLOAT));
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_NULL_LONG));
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_NULL_ZONEDDATETIME));
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_NULL_LOCALDATETIME));
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_NULL_LOCALDATE));
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_NULL_BYTE));
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_NULL_BOOLEAN));
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_NULL_BYTES));


	}



	public static String getNullMethodName(Type columnType) {
		return "getNull" + columnType.getName();
	}

	
}
