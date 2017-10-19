package php.cls.expression;

import php.Types;
import php.cls.Type;

public class BoolExpression extends Expression{

	protected boolean b;
	
	public BoolExpression(boolean b) {
		this.b = b;
	}

	@Override
	public Type getType() {
		return Types.Bool;
	}
	
	@Override
	public String toString() {
		return Boolean.toString(b);
	}
	
	public static final BoolExpression TRUE =new BoolExpression(true);
	public static final BoolExpression FALSE =new BoolExpression(false);

	
}
