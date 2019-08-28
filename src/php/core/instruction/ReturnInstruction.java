package php.core.instruction;

import php.core.expression.Expression;
import util.CodeUtil2;

public class ReturnInstruction extends Instruction{
	
	Expression returnExpression;
	
	public ReturnInstruction() {
		this.returnExpression = null;
	}
	
	public ReturnInstruction(Expression returnExpression) {
		this.returnExpression = returnExpression;
	}
	
	@Override
	public String toString() {
		return CodeUtil2.sp("return", returnExpression != null ? returnExpression.getUsageString() : null)+';';
	}
	
}
