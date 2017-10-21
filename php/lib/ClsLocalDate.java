package php.lib;

import php.core.PhpCls;

public class ClsLocalDate extends PhpCls {

	public static final String METHOD_NAME_NOW = "now";

	public ClsLocalDate() {
		super("LocalDate", "java.time");
		addMethod(new StaticLibMethod(this, METHOD_NAME_NOW));
	}

	

}
