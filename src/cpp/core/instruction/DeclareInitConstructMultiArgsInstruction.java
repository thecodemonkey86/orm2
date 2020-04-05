package cpp.core.instruction;

import java.util.ArrayList;

import cpp.core.QStringLiteral;
import cpp.core.expression.Expression;
import cpp.core.expression.Var;
import util.CodeUtil2;

public class DeclareInitConstructMultiArgsInstruction extends Instruction {
	
	protected Var var;
	protected Expression[] init;
	
	public DeclareInitConstructMultiArgsInstruction(Var var,Expression[] init) {
		this.var = var;
		this.init = init;
	}
	
	@Override
	public String toString() {
		ArrayList<String> a = new ArrayList<>();
		for(Expression e:init) {
			a.add((e instanceof QStringLiteral ? ((QStringLiteral)e).getExpression() : e) .toString());
		}
		
		
		return CodeUtil2.sp(var.getType().toUsageString(),var.getName(),CodeUtil2.parentheses(
			 CodeUtil2.commaSep(a)))+';';
	}
}
