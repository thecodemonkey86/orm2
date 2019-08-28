package php.core.expression;

import php.core.Type;
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

}
