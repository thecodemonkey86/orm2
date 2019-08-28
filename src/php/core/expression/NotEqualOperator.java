package php.core.expression;

import php.core.Operator;
import php.core.Types;

public class NotEqualOperator extends Operator {

	public static final NotEqualOperator INSTANCE = new NotEqualOperator();

	public NotEqualOperator() {
		super("!==", Types.Bool, false);
	}

	@Override
	public void addImplementation() {
		
	}

}
