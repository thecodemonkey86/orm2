package cpp.core.expression;

import cpp.Types;
import cpp.core.Type;

public class UIntExpression extends Expression{
	long s;
	
	public UIntExpression(long s) {
		this.s=s;
	}
	
	@Override
	public Type getType() {
		return Types.SizeT;
	}

	@Override
	public String toString() {
		return Long.toString(s)+"u";
	}
}
