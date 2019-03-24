package sunjava.core.expression;

import sunjava.core.Type;
import sunjava.core.Types;

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
