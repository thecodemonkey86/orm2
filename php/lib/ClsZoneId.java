package php.lib;

import php.core.PhpCls;

public class ClsZoneId extends PhpCls{

	public static final String METHOD_NAME_SYSTEM_DEFAULT = "systemDefault";
	
	public ClsZoneId() {
		super("ZoneId", "java.time");
		addMethod(new StaticLibMethod(this, METHOD_NAME_SYSTEM_DEFAULT));
	}

}
