package cpp.core.expression;

import cpp.Types;
import cpp.core.Type;

public class IntExpression extends Expression {

	int i;
	
	public IntExpression(int i) {
		this.i=i;
	}
	
	@Override
	public Type getType() {
		return Types.Int;
	}

	@Override
	public String toString() {
		return Integer.toString(i);
	}

}
