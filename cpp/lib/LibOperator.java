package cpp.lib;

import cpp.cls.Operator;
import cpp.cls.Type;

public class LibOperator extends Operator {

	public LibOperator(String symbol, Type returnType, boolean constQualifier) {
		super(symbol, returnType, constQualifier);
	}

	@Override
	public void addImplementation() {
	}

}
