package php.core.expression;

import php.core.Type;
import php.core.Types;

public class DoubleExpression extends Expression {

	double d;
	
	public DoubleExpression(double d) {
		this.d = d;
	}
	
	@Override
	public Type getType() {
		return Types.Float;
	}

	@Override
	public String toString() {
		return Double.toString(d);
	}


}
