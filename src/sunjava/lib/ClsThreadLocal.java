package sunjava.lib;

import sunjava.core.JavaGenericClass;
import sunjava.core.Type;
import sunjava.core.Types;

public class ClsThreadLocal extends JavaGenericClass {

	public static final String withInitial = "withInitial";
	public static final String get = "get";
	public static final String set = "set";

	public ClsThreadLocal(Type type) {
		super("ThreadLocal", type, null);
		addMethod(new LibMethod(type, get));
		addMethod(new LibMethod(Types.Void, set));
		addMethod(new StaticLibMethod(Types.supplier(type),withInitial));
	}

}
