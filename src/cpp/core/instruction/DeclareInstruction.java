package cpp.core.instruction;

import cpp.core.expression.Expression;
import cpp.core.expression.Var;
import util.CodeUtil2;

public class DeclareInstruction extends Instruction {
	
	protected Var var;
	protected Expression init;
	
	public DeclareInstruction(Var var, Expression init) {
		this.var = var;
		this.init = init;
	}
	
	@Override
	public String toString() {
		//return CodeUtil2.sp(var.getType().toUsageString(),var.getName(),init!=null?CodeUtil2.parentheses(init):"")+';';
		return CodeUtil2.sp(var.getType().toUsageString(),var.getName(),init!=null?" = " + init:"")+';';
	}
	
	public Var getVar() {
		return var;
	}
}
