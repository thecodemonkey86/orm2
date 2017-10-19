package sunjava.cls.expression;

public class PlusOperatorExpression extends BinaryOperatorExpression {

	public PlusOperatorExpression(Expression q1, Expression q2) {
		super(q1, new PlusOperator(q1.getType()), q2);
	}
	
	

}
