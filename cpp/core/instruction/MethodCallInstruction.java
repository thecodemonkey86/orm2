package cpp.core.instruction;

import cpp.core.MethodCall;

public class MethodCallInstruction extends ScClosedInstruction {

	protected MethodCall call;
	
	public MethodCallInstruction(MethodCall call) {
		super(call.toString());
		this.call = call;
	}
	
	public MethodCall getCall() {
		return call;
	}

}
