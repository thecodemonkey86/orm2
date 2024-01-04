package cpp.core.instruction;

import cpp.core.expression.MethodCall;

public class MethodCallInstruction extends SemicolonTerminatedInstruction {

	protected MethodCall call;
	
	public MethodCallInstruction(MethodCall call) {
		super(call.toString());
		this.call = call;
	}
	
	public MethodCall getCall() {
		return call;
	}

}
