package php.core.expression;

import php.core.Type;
import php.lib.LibOperator;

public class PlusOperator extends LibOperator {

	public PlusOperator(Type returnType) {
		super("+", returnType, false);
	}

}
