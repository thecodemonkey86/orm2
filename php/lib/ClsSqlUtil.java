package php.lib;

import php.Types;
import php.cls.PhpCls;

public class ClsSqlUtil extends PhpCls{

	public static final String getPlaceholders = "getPlaceholders";
	public static final String getPlaceholdersMultipleRows = "getPlaceholdersMultipleRows";
//	public static final String getPlaceholdersMultipleColumns = "getPlaceholdersMultipleColumns";

	public ClsSqlUtil() {
		super("SqlUtil", "Sql\\Util");
		addMethod(new StaticLibMethod(Types.String, getPlaceholders));
		addMethod(new StaticLibMethod(Types.String, getPlaceholdersMultipleRows));
//		addMethod(new StaticLibMethod(Types.String, getPlaceholdersMultipleColumns));
	}

}
