package cpp.core.expression;

import cpp.Types;
import cpp.core.Type;

public class UInt8Expression extends Expression{
	short s;
	
	public UInt8Expression(short s) {
		this.s=s;
	}
	
	@Override
	public Type getType() {
		return Types.UInt8;
	}

	@Override
	public String toString() {
		return Short.toString(s)+"u";
	}
}
