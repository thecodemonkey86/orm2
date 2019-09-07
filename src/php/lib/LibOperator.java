package php.lib;

import php.core.Operator;
import php.core.Type;

public class LibOperator extends Operator {

	public LibOperator(String symbol, Type returnType, boolean constQualifier) {
		super(symbol, returnType, constQualifier);
	}

	@Override
	public void addImplementation() {
	}

}
