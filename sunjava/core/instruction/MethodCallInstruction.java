package sunjava.core.instruction;

import sunjava.core.AbstractJavaCls;
import sunjava.core.MethodCall;

public class MethodCallInstruction extends ScClosedInstruction {

	protected MethodCall call;
	
	public MethodCallInstruction(MethodCall call) {
		super(call.toString());
		this.call = call;
	}
	
	public MethodCall getCall() {
		return call;
	}
	
	@Override
	public void collectImports(AbstractJavaCls cls) {
		call.collectImports(cls);
	}

}
