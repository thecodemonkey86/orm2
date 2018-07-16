package php.lib;

import php.core.PhpCls;
import php.core.Type;
import util.StringUtil;

public class ClsSqlParam extends PhpCls{

	public static final String METHOD_NAME_GET_STRING = "getString";
	public static final String METHOD_NAME_GET_INT = "getInt";
	public static final String METHOD_NAME_GET_FLOAT = "getFloat";
	public static final String METHOD_NAME_GET_NULL_STRING = "getNullString";
	public static final String METHOD_NAME_GET_NULL_INT = "getNullInt";
	public static final String METHOD_NAME_GET_NULL_FLOAT= "getNullFloat";
	public static final String METHOD_NAME_GET_DATE_TIME= "getDateTime";



	
	public ClsSqlParam() {
		super("SqlParam", "\\PhpLibs\\Sql\\Query");
		
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_STRING)); 
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_INT)); 
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_FLOAT));
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_NULL_STRING)); 
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_NULL_INT)); 
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_NULL_FLOAT));
		addMethod(new StaticLibMethod(this, METHOD_NAME_GET_DATE_TIME));


	}



	public static String getMethodName(Type columnType) {
		return "get" + StringUtil.ucfirst( columnType.getName());
	}
	
	public static String getNullMethodName(Type columnType) {
		return "getNull" + StringUtil.ucfirst( columnType.getName());
	}

	
}
