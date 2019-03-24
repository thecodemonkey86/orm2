package cpp.lib;

import cpp.core.Cls;

public class ClsQDate extends Cls{
	public static final String toString = "toString";
	public static final String fromString = "fromString";
	
	public ClsQDate() {
		super("QDate");
		addMethod(new LibMethod(this, "currentDate"));
		addMethod(new LibMethod(this, toString));
		addMethod(new LibMethod(this, fromString,true));
	}

}
