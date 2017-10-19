package php.cls.expression;

import php.cls.Type;
import php.lib.LibOperator;

public class PlusOperator extends LibOperator {

	public PlusOperator(Type returnType) {
		super("+", returnType, false);
	}

}
