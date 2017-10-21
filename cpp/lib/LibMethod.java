package cpp.lib;

import cpp.core.Method;
import cpp.core.Type;

public class LibMethod extends Method{

	public LibMethod(Type returnType, String name) {
		super(Public, returnType, name);
	}

	@Override
	public void addImplementation() {
	}

}
