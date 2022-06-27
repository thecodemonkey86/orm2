package php.core.expression;

import codegen.CodeUtil;
import php.core.Operator;
import php.core.Type;

public class BinaryOperatorExpression extends Expression {

	protected Expression expression;
	protected Operator op;
	protected Expression arg;
	
	public BinaryOperatorExpression(Expression expression, Operator op, Expression arg) {
		this.expression = expression;
		this.arg = arg;
		this.op = op;
	}
	
	@Override
	public Type getType() {
		return op.getReturnType();
	}
	
	public Operator getOp() {
		return op;
	}
	
	@Override
	public String toString() {
		return CodeUtil.sp(  expression.getUsageString(),op.getSymbol() ,arg.getUsageString());
	}

	public Expression getExpression() {
		return expression;
	}
	
	public Expression getArg() {
		return arg;
	}
}
