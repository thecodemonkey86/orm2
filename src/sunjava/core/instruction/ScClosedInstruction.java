package sunjava.core.instruction;

public class ScClosedInstruction extends Instruction{
	String instr;
	
	public ScClosedInstruction(String instr) {
		this.instr = instr;
	}

	@Override
	public String toString() {
		return instr+';';
	}
}
