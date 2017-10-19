package sunjava.cls.expression;

import sunjava.cls.Type;
import sunjava.lib.LibOperator;

public class PlusOperator extends LibOperator {

	public PlusOperator(Type returnType) {
		super("+", returnType, false);
	}

}
