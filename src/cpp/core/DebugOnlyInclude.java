package cpp.core;

import util.CodeUtil2;

public class DebugOnlyInclude extends Include {
	
	Include i;
	
	public DebugOnlyInclude(Include i) {
		this.i = i;
	}
	
	@Override
	public String toString() {
		return "#ifdef QT_DEBUG" + CodeUtil2.NEWLINE + i + CodeUtil2.NEWLINE + "#endif";
	}
}
