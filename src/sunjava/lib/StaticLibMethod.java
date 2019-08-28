package sunjava.lib;

import sunjava.core.Type;

public class StaticLibMethod extends LibMethod {

	public StaticLibMethod(Type returnType, String name) {
		super(returnType, name);
		setStatic(true);
	}

}
