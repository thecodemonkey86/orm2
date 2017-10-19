package sunjava.cls.expression;

import sunjava.cls.AbstractJavaCls;
import sunjava.cls.Operator;
import sunjava.cls.Type;
import sunjava.cls.expression.Expression;

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
	
	@Override
	public void collectImports(AbstractJavaCls cls) {
		expression.collectImports(cls);
	}

}
