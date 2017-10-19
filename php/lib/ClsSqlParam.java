package php.lib;

import php.cls.PhpCls;
import php.cls.Type;
import util.StringUtil;

public class ClsSqlParam extends PhpCls{

	public static final String METHOD_NAME_STRING = "getString";
	public static final String METHOD_NAME_INT = "getInt";
	public static final String METHOD_NAME_FLOAT = "getFloat";
	public static final String METHOD_NAME_NULL_STRING = "getNullString";
	public static final String METHOD_NAME_NULL_INT = "getNullInt";
	public static final String METHOD_NAME_NULL_FLOAT= "getNullFloat";



	
	public ClsSqlParam() {
		super("SqlParam", "\\PhpLibs\\Sql\\Query");
		
		addMethod(new StaticLibMethod(this, METHOD_NAME_STRING)); 
		addMethod(new StaticLibMethod(this, METHOD_NAME_INT)); 
		addMethod(new StaticLibMethod(this, METHOD_NAME_FLOAT));
		addMethod(new StaticLibMethod(this, METHOD_NAME_NULL_STRING)); 
		addMethod(new StaticLibMethod(this, METHOD_NAME_NULL_INT)); 
		addMethod(new StaticLibMethod(this, METHOD_NAME_NULL_FLOAT));


	}



	public static String getMethodName(Type columnType) {
		return "get" + StringUtil.ucfirst( columnType.getName());
	}
	
	public static String getNullMethodName(Type columnType) {
		return "getNull" + StringUtil.ucfirst( columnType.getName());
	}

	
}
