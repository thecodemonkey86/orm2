package sunjava.core;

import sunjava.lib.LibOperator;

public class IncrementOperator extends LibOperator{

	public IncrementOperator(Type returnType) {
		super("++", returnType, false);
	}

}
