package cpp.core.instruction;

import generate.CodeUtil2;
import cpp.core.QStringLiteral;
import cpp.core.expression.Expression;
import cpp.core.expression.Var;

public class DeclareInitConstructInstruction extends Instruction {
	
	protected Var var;
	protected Expression init;
	
	public DeclareInitConstructInstruction(Var var, Expression init) {
		this.var = var;
		this.init = init;
	}
	
	@Override
	public String toString() {
		return CodeUtil2.sp(var.getType().toUsageString(),var.getName(),CodeUtil2.parentheses(
				(init instanceof QStringLiteral ? ((QStringLiteral)init).getExpression() : init) ))+';';
	}
}
