package sunjava.core.instruction;

import sunjava.core.AbstractJavaCls;
import sunjava.core.expression.Expression;
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
		return CodeUtil2.sp("return", returnExpression)+';';
	}
	
	@Override
	public void collectImports(AbstractJavaCls cls) {
		if (returnExpression!=null)
			returnExpression.collectImports(cls);
	}
}
