package cpp.lib;

import cpp.core.Cls;

public class ClsQTime extends Cls{

	public ClsQTime() {
		super("QTime");
		addMethod(new LibMethod(this, "currentTime"));
	}

}
