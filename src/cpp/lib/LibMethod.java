package cpp.lib;

import cpp.core.Method;
import cpp.core.Type;

public class LibMethod extends Method{

	public LibMethod(Type returnType, String name) {
		super(Public, returnType, name);
	}
	
	public LibMethod(Type returnType, String name, boolean staticMethod) {
		super(Public, returnType, name);
		setStatic(staticMethod);
	}

	@Override
	public void addImplementation() {
	}

}
