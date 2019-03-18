package cpp.lib;

import cpp.core.Method;
import cpp.core.MethodTemplate;
import cpp.core.Type;

public abstract class LibMethodTemplate extends MethodTemplate{

	public LibMethodTemplate( Type returnType, String name) {
		super(Method.Public, returnType, name);
	}


}
