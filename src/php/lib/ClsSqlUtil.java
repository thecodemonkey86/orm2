package php.lib;

import php.core.PhpCls;
import php.core.Types;

public class ClsSqlUtil extends PhpCls{

	public static final String getPlaceholders = "getPlaceholders";
	public static final String getPlaceholdersMultipleRows = "getPlaceholdersMultipleRows";
//	public static final String getPlaceholdersMultipleColumns = "getPlaceholdersMultipleColumns";

	public ClsSqlUtil() {
		super("SqlUtil", "PhpLibs\\Sql\\Util");
		addMethod(new StaticLibMethod(Types.String, getPlaceholders));
		addMethod(new StaticLibMethod(Types.String, getPlaceholdersMultipleRows));
//		addMethod(new StaticLibMethod(Types.String, getPlaceholdersMultipleColumns));
	}

}
