package cpp.lib;

import cpp.cls.Cls;

public class ClsQDateTime extends Cls{

	public ClsQDateTime() {
		super("QDateTime");
		addMethod(new LibMethod(this, "currentDateTime"));
	}

}
