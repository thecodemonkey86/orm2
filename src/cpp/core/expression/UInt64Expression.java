package cpp.core.expression;

import cpp.Types;
import cpp.core.Type;

public class UInt64Expression extends Expression{
	long s;
	
	public UInt64Expression(long s) {
		this.s=s;
	}
	
	@Override
	public Type getType() {
		return Types.UInt64;
	}

	@Override
	public String toString() {
		return Long.toString(s)+"u";
	}
}
