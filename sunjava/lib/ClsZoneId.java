package sunjava.lib;

import sunjava.cls.JavaCls;

public class ClsZoneId extends JavaCls{

	public static final String METHOD_NAME_SYSTEM_DEFAULT = "systemDefault";
	
	public ClsZoneId() {
		super("ZoneId", "java.time");
		addMethod(new StaticLibMethod(this, METHOD_NAME_SYSTEM_DEFAULT));
	}

}
