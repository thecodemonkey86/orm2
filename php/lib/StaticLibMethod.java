package php.lib;

import php.core.Type;

public class StaticLibMethod extends LibMethod {

	public StaticLibMethod(Type returnType, String name) {
		super(returnType, name);
		setStatic(true);
	}

}
