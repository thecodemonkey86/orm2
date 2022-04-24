package cpp.core.instruction;

import cpp.core.expression.Expression;
import util.CodeUtil2;

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
	
	public Expression getAssign() {
		return assign;
	}
	
	public Expression getValue() {
		return value;
	}

	public String toConstructorInitializeString() {
		return assign.toConstructorInitializeString();
	}
	
}
