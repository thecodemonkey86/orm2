package sunjava.lib;

import sunjava.Types;
import sunjava.cls.JavaCls;

public class ClsSqlUtil extends JavaCls{

	public static final String getPlaceholders = "getPlaceholders";
	public static final String getPlaceholdersMultipleRows = "getPlaceholdersMultipleRows";
//	public static final String getPlaceholdersMultipleColumns = "getPlaceholdersMultipleColumns";

	public ClsSqlUtil() {
		super("SqlUtil", "sql.util");
		addMethod(new StaticLibMethod(Types.String, getPlaceholders));
		addMethod(new StaticLibMethod(Types.String, getPlaceholdersMultipleRows));
//		addMethod(new StaticLibMethod(Types.String, getPlaceholdersMultipleColumns));
	}

}
