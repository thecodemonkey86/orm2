package cpp.lib;

import cpp.core.Cls;

public class ClsQDate extends Cls{

	public ClsQDate() {
		super("QDate");
		addMethod(new LibMethod(this, "currentDate"));
	}

}
