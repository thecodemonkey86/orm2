package cpp.core.expression;

import cpp.Types;
import cpp.core.Type;

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
