package php.core.instruction;

public abstract class Instruction {
	protected StackTraceElement[] stackTrace;
	
	
	public Instruction() {
		stackTrace = Thread.currentThread().getStackTrace();
	}
	
	public static SemicolonTerminatedInstruction sc(String instr) {
		return new SemicolonTerminatedInstruction(instr);
	}
	
	public void setStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace = stackTrace;
	}
	
	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}
	
}
