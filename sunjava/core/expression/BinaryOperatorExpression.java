package sunjava.core.expression;

import codegen.CodeUtil;
import sunjava.core.AbstractJavaCls;
import sunjava.core.Operator;
import sunjava.core.Type;
import sunjava.core.expression.Expression;

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
		return CodeUtil.sp(  expression,op.getSymbol() ,arg);
	}

	@Override
	public void collectImports(AbstractJavaCls cls) {
		expression.collectImports(cls);
		arg.collectImports(cls);
	}

	public Expression getExpression() {
		return expression;
	}
	
	public Expression getArg() {
		return arg;
	}
}
