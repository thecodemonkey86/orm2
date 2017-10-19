package sunjava.cls.expression;

import sunjava.Types;
import sunjava.cls.Operator;

public class NotEqualOperator extends Operator {

	public static final NotEqualOperator INSTANCE = new NotEqualOperator();

	public NotEqualOperator() {
		super("!=", Types.Bool, false);
	}

	@Override
	public void addImplementation() {
		
	}

}
