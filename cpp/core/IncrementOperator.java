package cpp.core;

import cpp.lib.LibOperator;

public class IncrementOperator extends LibOperator{

	public IncrementOperator(Type returnType) {
		super("++", returnType, false);
	}

}
