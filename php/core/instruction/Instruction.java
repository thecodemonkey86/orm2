package php.core.instruction;

public abstract class Instruction {
	protected StackTraceElement[] stackTrace;
	
	
	public Instruction() {
		stackTrace = Thread.currentThread().getStackTrace();
	}
	
	public static ScClosedInstruction sc(String instr) {
		return new ScClosedInstruction(instr);
	}
	
	public void setStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace = stackTrace;
	}
	
	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}
	
}
