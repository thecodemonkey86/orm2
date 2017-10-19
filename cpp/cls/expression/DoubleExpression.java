package cpp.cls.expression;

import cpp.Types;
import cpp.cls.Type;

public class DoubleExpression extends Expression {

	double d;
	
	public DoubleExpression(double d) {
		this.d = d;
	}
	
	@Override
	public Type getType() {
		return Types.Double;
	}

	@Override
	public String toString() {
		return Double.toString(d);
	}

}
