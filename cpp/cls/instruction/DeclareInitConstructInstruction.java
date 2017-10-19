package cpp.cls.instruction;

import generate.CodeUtil2;
import cpp.cls.QStringLiteral;
import cpp.cls.expression.Expression;
import cpp.cls.expression.Var;

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
