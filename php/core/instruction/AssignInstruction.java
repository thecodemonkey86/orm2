package php.core.instruction;

import generate.CodeUtil2;
import php.core.expression.Expression;

public class AssignInstruction extends Instruction {
	
	protected Expression assign;
	protected Expression value;
	
	public AssignInstruction(Expression assign, Expression value) {
		this.assign = assign;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return CodeUtil2.sp(assign.getWriteAccessString(),'=',value)+';';
	}
	
	
	
}
