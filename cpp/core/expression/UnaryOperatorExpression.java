package cpp.core.expression;

import cpp.core.Operator;
import cpp.core.Type;
import cpp.core.expression.Expression;

public class UnaryOperatorExpression extends Expression {

	protected Expression expression;
	protected Operator op;
	
	public UnaryOperatorExpression(Expression expression, Operator op) {
		this.expression = expression;
		this.op = op;
	}
	
	@Override
	public Type getType() {
		return op.getReturnType();
	}
	
	@Override
	public String toString() {
		return expression.toString()+op.getSymbol();
	}

}
