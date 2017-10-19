package sunjava.cls.expression;

import sunjava.Types;
import sunjava.cls.Type;

public class LongExpression extends Expression {

	long i;
	
	public LongExpression(long i) {
		this.i=i;
	}
	
	@Override
	public Type getType() {
		return Types.Long;
	}

	@Override
	public String toString() {
		return Long.toString(i);
	}


}
