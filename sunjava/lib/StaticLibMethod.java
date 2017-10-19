package sunjava.lib;

import sunjava.cls.Type;

public class StaticLibMethod extends LibMethod {

	public StaticLibMethod(Type returnType, String name) {
		super(returnType, name);
		setStatic(true);
	}

}
