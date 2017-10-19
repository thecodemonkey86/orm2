package php.cls.instruction;

import generate.CodeUtil2;
import php.cls.expression.Expression;
import php.cls.expression.Var;

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
