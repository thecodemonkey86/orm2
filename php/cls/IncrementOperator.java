package php.cls;

import php.lib.LibOperator;

public class IncrementOperator extends LibOperator{

	public IncrementOperator(Type returnType) {
		super("++", returnType, false);
	}

}
