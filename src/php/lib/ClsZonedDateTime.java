package php.lib;

import php.core.PhpCls;

public class ClsZonedDateTime extends PhpCls {

	public static final String METHOD_NAME_NOW = "now";
	public static final String METHOD_NAME_OF_INSTANT = "ofInstant";
	
	public ClsZonedDateTime() {
		super("ZonedDateTime", "java.time");
		addMethod(new StaticLibMethod(this, METHOD_NAME_NOW));
		addMethod(new StaticLibMethod(this, METHOD_NAME_OF_INSTANT));
	}


}
