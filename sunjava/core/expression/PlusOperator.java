package sunjava.core.expression;

import sunjava.core.Type;
import sunjava.lib.LibOperator;

public class PlusOperator extends LibOperator {

	public PlusOperator(Type returnType) {
		super("+", returnType, false);
	}

}
