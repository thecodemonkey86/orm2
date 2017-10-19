package cpp.cls.expression;

import cpp.cls.Type;
import cpp.lib.LibOperator;

public class PlusOperator extends LibOperator {

	public PlusOperator(Type returnType) {
		super("+", returnType, false);
	}

}
