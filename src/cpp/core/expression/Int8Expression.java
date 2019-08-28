package cpp.core.expression;

import cpp.Types;
import cpp.core.Type;

public class Int8Expression extends Expression {

	byte i;
	
	public Int8Expression(byte i) {
		this.i=i;
	}
	
	@Override
	public Type getType() {
		return Types.Int8;
	}

	@Override
	public String toString() {
		return Byte.toString(i);
	}

}
