package sunjava.core.expression;

import codegen.CodeUtil;
import sunjava.core.AbstractJavaCls;
import sunjava.core.Type;

public class ArrayAccessExpression extends Expression {

	protected Expression expr, arrayIndex;
	
	public ArrayAccessExpression(Expression expr, Expression arrayIndex) {
		this.expr = expr;
		this.arrayIndex = arrayIndex;
	}
	
	@Override
	public Type getType() {
		return ((IArrayAccessible)expr.getType()).getAccessType();
	}

	@Override
	public String toString() {
		return expr + CodeUtil.brackets(arrayIndex);
	}

	@Override
	public void collectImports(AbstractJavaCls cls) {
		expr.collectImports(cls);
		arrayIndex.collectImports(cls);
	}

}
