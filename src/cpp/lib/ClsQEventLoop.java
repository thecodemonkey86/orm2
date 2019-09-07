package cpp.lib;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.QtSlot;

public class ClsQEventLoop extends Cls{

	
	public static final QtSlot quit = new QtSlot(Method.Public, "quit");
	public static final String exec = "exec";

	public ClsQEventLoop() {
		super("QEventLoop");
		addMethod(quit);
		addMethod(new LibMethod(Types.Void, exec));
	}

}
