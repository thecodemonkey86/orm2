package cpp.core.expression;

import cpp.Types;
import cpp.core.Type;

public class Int16Expression extends Expression {

	short i;
	
	public Int16Expression(short i) {
		this.i=i;
	}
	
	@Override
	public Type getType() {
		return Types.Int16;
	}

	@Override
	public String toString() {
		return Short.toString(i);
	}

}
