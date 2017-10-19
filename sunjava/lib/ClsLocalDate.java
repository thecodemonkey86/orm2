package sunjava.lib;

import sunjava.cls.JavaCls;

public class ClsLocalDate extends JavaCls {

	public static final String METHOD_NAME_NOW = "now";

	public ClsLocalDate() {
		super("LocalDate", "java.time");
		addMethod(new StaticLibMethod(this, METHOD_NAME_NOW));
	}

	

}
