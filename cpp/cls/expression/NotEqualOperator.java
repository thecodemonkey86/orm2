package cpp.cls.expression;

import cpp.Types;
import cpp.cls.Operator;

public class NotEqualOperator extends Operator {

	public NotEqualOperator() {
		super("!=", Types.Bool, false);
	}

	@Override
	public void addImplementation() {
		
	}

}
