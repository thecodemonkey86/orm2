package sunjava.core.expression;

import sunjava.core.Type;
import sunjava.core.Types;

public class ShortExpression extends Expression {

	short s;
	
	public ShortExpression(short s) {
		this.s=s;
	}
	
	@Override
	public Type getType() {
		return Types.Short;
	}

	@Override
	public String toString() {
		return Short.toString(s);
	}


}
