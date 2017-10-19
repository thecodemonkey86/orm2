package sunjava.cls.expression;

import sunjava.Types;
import sunjava.cls.Type;

public class IntExpression extends Expression {

	int i;
	
	public IntExpression(int i) {
		this.i=i;
	}
	
	@Override
	public Type getType() {
		return Types.Int;
	}

	@Override
	public String toString() {
		return Integer.toString(i);
	}


}
