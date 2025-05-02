package cpp.core.expression;

import cpp.Types;
import cpp.core.Type;

public class UInt16Expression extends Expression{
	int s;
	
	public UInt16Expression(int s) {
		this.s=s;
	}
	
	@Override
	public Type getType() {
		return Types.UInt16;
	}

	@Override
	public String toString() {
		return Integer.toString(s)+"u";
	}
}
