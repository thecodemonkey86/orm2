package cpp.core;

import codegen.CodeUtil;

public class HeaderFileInclude extends Include{
	String file;
	
	public HeaderFileInclude(String i) {
		if(i==null) {
			throw new NullPointerException();
		}
		file = i;
		if(!i.endsWith(".h")) {
			file+=".h";
		}
	}

	@Override
	public String toString() {
		return "#include " + CodeUtil.quote(file);
	}
	
	@Override
	public int hashCode() {
		return file.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		return file.equals(((HeaderFileInclude)obj).file);
	}
}
