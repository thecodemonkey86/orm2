package cpp.lib;

import cpp.cls.Cls;

public class ClsQTime extends Cls{

	public ClsQTime() {
		super("QTime");
		addMethod(new LibMethod(this, "currentTime"));
	}

}
