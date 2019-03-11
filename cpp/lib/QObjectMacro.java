package cpp.lib;

import cpp.core.Constructor;

public class QObjectMacro extends Constructor {

	@Override
	public void addImplementation() {
	}

	@Override
	public String toString() {
		return "";
	}
	
	@Override
	public String toHeaderString() {
		return "Q_OBJECT";
	}
}
