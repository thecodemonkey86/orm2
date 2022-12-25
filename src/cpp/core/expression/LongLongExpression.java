package cpp.core.expression;

import cpp.Types;
import cpp.core.Type;

public class LongLongExpression extends Expression{

	long l;
	
	public LongLongExpression(long l) {
		this.l = l;
	}
	
	@Override
	public Type getType() {
		return Types.Int64;
	}

	@Override
	public String toString() {
		return Long.toString(l)+"LL";
	}

}
