package cpp.lib;

import cpp.cls.Cls;

public class ClsQDate extends Cls{

	public ClsQDate() {
		super("QDate");
		addMethod(new LibMethod(this, "currentDate"));
	}

}
