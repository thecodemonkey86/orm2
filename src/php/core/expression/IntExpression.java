package php.core.expression;

import php.core.Type;
import php.core.Types;

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
