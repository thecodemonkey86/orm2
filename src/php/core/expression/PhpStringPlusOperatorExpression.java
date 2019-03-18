package php.core.expression;

import php.core.PhpStringConcatOperator;

public class PhpStringPlusOperatorExpression extends BinaryOperatorExpression {

	public PhpStringPlusOperatorExpression(Expression q1, Expression q2) {
		super(q1, new PhpStringConcatOperator(), q2);
	}
	
	
	public PhpStringPlusOperatorExpression(Var q1, Var q2) {
		super(q1, new PhpStringConcatOperator(), q2);
	}
	
	public PhpStringPlusOperatorExpression(Var q1, PhpStringLiteral q2) {
		super(q1, new PhpStringConcatOperator(), q2);
	}
	
	public PhpStringPlusOperatorExpression(PhpStringLiteral q1, Var q2) {
		super(q1, new PhpStringConcatOperator(), q2);
	}
	
	public PhpStringPlusOperatorExpression(PhpStringLiteral q1, PhpStringLiteral q2) {
		super(q1, new PhpStringConcatOperator(), q2);
	}
	
	public PhpStringPlusOperatorExpression(PhpStringPlusOperatorExpression q1, PhpStringLiteral q2) {
		super(q1, new PhpStringConcatOperator(), q2);
	}
	
	public PhpStringPlusOperatorExpression(PhpStringPlusOperatorExpression q1, PhpStringPlusOperatorExpression q2) {
		super(q1, new PhpStringConcatOperator(), q2);
	}
	
	public PhpStringPlusOperatorExpression(PhpStringPlusOperatorExpression q1, Var q2) {
		super(q1, new PhpStringConcatOperator(), q2);
	}


	public PhpStringPlusOperatorExpression concat(PhpStringLiteral qs) {
		return new PhpStringPlusOperatorExpression(this, qs);
	}
	
	public PhpStringPlusOperatorExpression concat(Var qs) {
		return new PhpStringPlusOperatorExpression(this, qs);
	}
	
	public PhpStringPlusOperatorExpression concat(PhpStringPlusOperatorExpression qs) {
		return new PhpStringPlusOperatorExpression(this, qs);
	}

}
