package php.core.instruction;

import generate.CodeUtil2;
import php.core.expression.Expression;
import php.core.expression.Var;

public class DeclareInstruction extends Instruction {
	
	protected Var var;
	protected Expression init;
	
	public DeclareInstruction(Var var, Expression init) {
		this.var = var;
		this.init = init;
	}
	
	@Override
	public String toString() {
		return CodeUtil2.sp("$"+var.getName(),init!=null?" = " + init.getUsageString():"")+';';
	}
	
	public Var getVar() {
		return var;
	}
	
}
