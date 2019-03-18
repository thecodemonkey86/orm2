package sunjava.core.expression;

import sunjava.core.Operator;
import sunjava.core.Types;

public class NotEqualOperator extends Operator {

	public static final NotEqualOperator INSTANCE = new NotEqualOperator();

	public NotEqualOperator() {
		super("!=", Types.Bool, false);
	}

	@Override
	public void addImplementation() {
		
	}

}
