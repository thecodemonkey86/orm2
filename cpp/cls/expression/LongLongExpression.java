package cpp.cls.expression;

import cpp.Types;
import cpp.cls.Type;

public class LongLongExpression extends Expression{

	long l;
	
	public LongLongExpression(long l) {
		this.l = l;
	}
	
	@Override
	public Type getType() {
		return Types.LongLong;
	}

	@Override
	public String toString() {
		return Long.toString(l)+"LL";
	}

}
