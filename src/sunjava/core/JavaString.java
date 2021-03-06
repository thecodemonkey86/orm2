package sunjava.core;

import codegen.CodeUtil;
import sunjava.core.expression.CharExpression;
import sunjava.core.expression.Expression;
import sunjava.core.expression.JavaStringLiteral;
import sunjava.core.expression.JavaStringPlusOperatorExpression;

public class JavaString extends Expression {

	protected Expression expression;
	
	public JavaString(Expression expression) {
		this.expression = expression;
	}
	
	public static JavaString stringConstant(String str) {
		return new JavaString(new JavaStringLiteral(str));
	}
	
	@Deprecated
	public static JavaString fromExpression(Expression expression) {
		return new JavaString(expression);
	}
	
	@Override
	public Type getType() {
		return Types.String;
	}
	
	@Override
	public String toString() {
		return expression.getType() == Types.String ? expression.toString() : "new String"+CodeUtil.parentheses(expression.toString());
	}
	
	public Expression getExpression() {
		return expression;
	}

	public JavaStringPlusOperatorExpression concat(JavaString qString) {
		return new JavaStringPlusOperatorExpression(this, qString);
	}
	
	public JavaStringPlusOperatorExpression concat(Expression expression) {
		return new JavaStringPlusOperatorExpression(this, expression);
	}
	
	public JavaStringPlusOperatorExpression concat(CharExpression chr) {
		return new JavaStringPlusOperatorExpression(this, chr);
	}


}
