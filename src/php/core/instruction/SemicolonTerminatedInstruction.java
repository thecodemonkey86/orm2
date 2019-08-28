package php.core.instruction;

public class SemicolonTerminatedInstruction extends Instruction{
	String instr;
	
	public SemicolonTerminatedInstruction(String instr) {
		this.instr = instr;
	}

	@Override
	public String toString() {
		return instr+';';
	}
}
