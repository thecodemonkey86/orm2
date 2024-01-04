package php.core.instruction;

public abstract class Instruction {
	protected StackTraceElement[] stackTrace;
	protected static boolean isStackTraceEnabled;
	
	public Instruction() {
		if(isStackTraceEnabled)
			stackTrace = Thread.currentThread().getStackTrace();
	}
	
	public static void setStackTraceEnabled(boolean isStackTraceEnabled) {
		Instruction.isStackTraceEnabled = isStackTraceEnabled;
	}
	
	public static SemicolonTerminatedInstruction sc(String instr) {
		return new SemicolonTerminatedInstruction(instr);
	}
	
	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}
	
}
