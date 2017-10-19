package php.cls.instruction;

import php.cls.MethodCall;

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
