package php.core.expression;

import php.core.Operator;
import php.core.Type;

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
