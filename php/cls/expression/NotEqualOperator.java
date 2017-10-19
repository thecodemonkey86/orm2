package php.cls.expression;

import php.Types;
import php.cls.Operator;

public class NotEqualOperator extends Operator {

	public static final NotEqualOperator INSTANCE = new NotEqualOperator();

	public NotEqualOperator() {
		super("!==", Types.Bool, false);
	}

	@Override
	public void addImplementation() {
		
	}

}
