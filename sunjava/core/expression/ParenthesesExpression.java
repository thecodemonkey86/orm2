package sunjava.core.expression;

import sunjava.core.AbstractJavaCls;
import sunjava.core.Type;
import util.CodeUtil2;

public class ParenthesesExpression extends Expression {

	Expression expression;
	
	public ParenthesesExpression(Expression expression) {
		this.expression = expression;
	}
	
	@Override
	public Type getType() {
		return expression.getType();
	}

	@Override
	public String toString() {
		return CodeUtil2.parentheses(expression);
	}

	@Override
	public void collectImports(AbstractJavaCls cls) {
		expression.collectImports(cls);
	}
}
