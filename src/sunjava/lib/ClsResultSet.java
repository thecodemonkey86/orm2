package sunjava.lib;

import sunjava.core.JavaCls;
import sunjava.core.Types;

public class ClsResultSet extends JavaCls{

	public static final String METHOD_NAME_NEXT = "next";
	public static final String wasNull = "wasNull";
	public static final String METHOD_NAME_GET_INT = "getInt";
	public static final String METHOD_NAME_GET_DOUBLE = "getDouble";
	public static final String METHOD_NAME_GET_LONG = "getLong";
	public static final String METHOD_NAME_GET_SHORT = "getShort";
	public static final String METHOD_NAME_GET_BOOLEAN = "getBoolean";
	public static final String METHOD_NAME_GET_STRING = "getString";
	public static final String METHOD_NAME_GET_TIMESTAMP = "getTimestamp";
	public static final String METHOD_NAME_GET_DATE = "getDate"; 
	public static final String METHOD_NAME_GET_BYTES = "getBytes"; 
	public static final String METHOD_NAME_GET_OBJECT = "getBytes"; 
	
	public ClsResultSet() {
		super("ResultSet", "java.sql");
		addMethod(new LibMethod(Types.Bool, METHOD_NAME_NEXT));
		addMethod(new LibMethod(Types.Bool, wasNull));
		addMethod(new LibMethod(Types.Int, METHOD_NAME_GET_INT));
		addMethod(new LibMethod(Types.Double, METHOD_NAME_GET_DOUBLE));
		addMethod(new LibMethod(Types.Long, METHOD_NAME_GET_LONG));
		addMethod(new LibMethod(Types.Long, METHOD_NAME_GET_BOOLEAN));
		addMethod(new LibMethod(Types.Short, METHOD_NAME_GET_SHORT));
		addMethod(new LibMethod(Types.String, METHOD_NAME_GET_STRING));
		addMethod(new LibMethod(Types.Timestamp, METHOD_NAME_GET_TIMESTAMP));
		addMethod(new LibMethod(Types.SqlDate, METHOD_NAME_GET_DATE));
		addMethod(new LibMethod(Types.ByteArray, METHOD_NAME_GET_BYTES));
		addMethod(new LibMethod(Types.Object, METHOD_NAME_GET_OBJECT));
	}

	

	
}
