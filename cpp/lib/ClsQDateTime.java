package cpp.lib;

import cpp.core.Cls;

public class ClsQDateTime extends Cls{
	public static final String toString = "toString";
	public static final String fromString = "fromString";
	
	public ClsQDateTime() {
		super("QDateTime");
		addMethod(new LibMethod(this, "currentDateTime"));
		addMethod(new LibMethod(this, toString));
		addMethod(new LibMethod(this, fromString,true));
	}

}
