package cpp.core.expression;

import cpp.Types;
import cpp.core.Type;

public class Int64Expression extends Expression {

	long i;
	
	public Int64Expression(long i) {
		this.i=i;
	}
	
	@Override
	public Type getType() {
		return Types.Int64;
	}

	@Override
	public String toString() {
		return Long.toString(i);
	}

}
