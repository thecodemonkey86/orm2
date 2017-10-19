package cpp.lib;

import cpp.cls.Method;
import cpp.cls.Type;

public class LibMethod extends Method{

	public LibMethod(Type returnType, String name) {
		super(Public, returnType, name);
	}

	@Override
	public void addImplementation() {
	}

}
