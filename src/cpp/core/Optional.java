package cpp.core;

import cpp.Namespaces;
import cpp.lib.LibMethod;

public class Optional extends TplCls {

	
	public static final String emplace = "emplace";
	public static final String value = "value";
	public static final String value_or = "value_or";
	public static final String has_value = "has_value";
	
	public Optional(Type type) {
		super("optional", type);
		setUseNamespace(Namespaces.std);
		addMethod(new LibMethod(type, value));
		addMethod(new LibMethod(type, value_or));
		addMethod(new LibMethod(type, has_value));
		addMethod(new LibMethod(type, emplace));
	}

}
