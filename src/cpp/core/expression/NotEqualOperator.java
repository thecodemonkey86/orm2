package cpp.core.expression;

import cpp.Types;
import cpp.core.Operator;

public class NotEqualOperator extends Operator {

	public NotEqualOperator() {
		super("!=", Types.Bool, false);
	}

	@Override
	public void addImplementation() {
		
	}

}
