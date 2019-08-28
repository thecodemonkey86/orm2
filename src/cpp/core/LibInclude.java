package cpp.core;

import codegen.CodeUtil;

public class LibInclude extends Include {
	
	String name;
	
	public LibInclude(String name) {
		this.name = name;
	}
	public LibInclude(Type type) {
		this.name = type.getName();
	}
	@Override
	public String toString() {
		return "#include "+ CodeUtil.abr(this.name);
	}
}
