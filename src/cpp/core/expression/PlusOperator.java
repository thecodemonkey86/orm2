package cpp.core.expression;

import cpp.core.Type;
import cpp.lib.LibOperator;

public class PlusOperator extends LibOperator {

	public static final String SYMBOL = "+";
	
	public PlusOperator(Type returnType) {
		super(SYMBOL, returnType, false);
	}

}
