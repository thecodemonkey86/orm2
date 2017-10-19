package php.cls.instruction;

import generate.CodeUtil2;
import php.cls.expression.Expression;

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
