package cpp.lib;

import cpp.core.MethodTemplate;
import cpp.core.Type;
import cpp.core.method.TplMethod;

public class LibTplMethod extends TplMethod {

	public LibTplMethod(MethodTemplate template,String visibility, Type returnType, String name, Type[] concreteTypes) {
		super(template, visibility, returnType, name,concreteTypes);
	}

	@Override
	public void addImplementation() {
	}

}
