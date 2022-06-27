package cpp.core.expression;

import codegen.CodeUtil;
import cpp.core.Operator;
import cpp.core.Type;

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
	
	@Override
	public String toString() {
		return CodeUtil.sp(  expression.getReadAccessString(),op.getSymbol() ,arg.getReadAccessString());
	}

}
