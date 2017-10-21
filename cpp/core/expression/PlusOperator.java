package cpp.core.expression;

import cpp.core.Type;
import cpp.lib.LibOperator;

public class PlusOperator extends LibOperator {

	public PlusOperator(Type returnType) {
		super("+", returnType, false);
	}

}
