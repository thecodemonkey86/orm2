package php.lib;

import php.cls.Method;
import php.cls.Type;

public class LibMethod extends Method{

	public LibMethod(Type returnType, String name) {
		super(Public, checkType(returnType), name);
	}

	@Override
	public void addImplementation() {
	}

	private static Type checkType(Type input) {
		if (input == null) {
			throw new NullPointerException();
		}
		return input;
	}
}
