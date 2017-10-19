package sunjava.cls.expression;

import sunjava.cls.JavaString;
import sunjava.cls.JavaStringConcatOperator;

public class JavaStringPlusOperatorExpression extends BinaryOperatorExpression {

	public JavaStringPlusOperatorExpression(Expression q1, Expression q2) {
		super(q1, new JavaStringConcatOperator(), q2);
	}
	
	
	public JavaStringPlusOperatorExpression(Var q1, Var q2) {
		super(q1, new JavaStringConcatOperator(), q2);
	}
	
	public JavaStringPlusOperatorExpression(Var q1, JavaString q2) {
		super(q1, new JavaStringConcatOperator(), q2);
	}
	
	public JavaStringPlusOperatorExpression(JavaString q1, Var q2) {
		super(q1, new JavaStringConcatOperator(), q2);
	}
	
	public JavaStringPlusOperatorExpression(JavaString q1, JavaString q2) {
		super(q1, new JavaStringConcatOperator(), q2);
	}
	
	public JavaStringPlusOperatorExpression(JavaStringPlusOperatorExpression q1, JavaString q2) {
		super(q1, new JavaStringConcatOperator(), q2);
	}
	
	public JavaStringPlusOperatorExpression(JavaStringPlusOperatorExpression q1, JavaStringPlusOperatorExpression q2) {
		super(q1, new JavaStringConcatOperator(), q2);
	}
	
	public JavaStringPlusOperatorExpression(JavaStringPlusOperatorExpression q1, Var q2) {
		super(q1, new JavaStringConcatOperator(), q2);
	}

	public JavaStringPlusOperatorExpression(JavaString q1, CharExpression chr) {
		super(q1, new JavaStringConcatOperator(), chr);
	}

	public JavaStringPlusOperatorExpression concat(JavaString qs) {
		return new JavaStringPlusOperatorExpression(this, qs);
	}
	
	public JavaStringPlusOperatorExpression concat(Var qs) {
		return new JavaStringPlusOperatorExpression(this, qs);
	}
	
	public JavaStringPlusOperatorExpression concat(JavaStringPlusOperatorExpression qs) {
		return new JavaStringPlusOperatorExpression(this, qs);
	}

}
