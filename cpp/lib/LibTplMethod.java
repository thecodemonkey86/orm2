package cpp.lib;

import cpp.core.Type;
import cpp.core.method.TplMethod;

public class LibTplMethod extends TplMethod {

	public LibTplMethod(String visibility, Type returnType, String name, Type[] concreteTypes) {
		super(visibility, returnType, name,concreteTypes);
	}

	@Override
	public void addImplementation() {
	}

}
