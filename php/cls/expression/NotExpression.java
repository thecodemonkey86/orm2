package php.cls.expression;

import php.Types;
import php.cls.AbstractPhpCls;
import php.cls.Type;

public class NotExpression extends Expression{
	protected Expression e;
	
	
	public NotExpression(Expression e) {
		super();
		this.e = e;
	}

	@Override
	public String toString() {
		return "!" + e.getUsageString();
	}
	
	@Override
	public String getUsageString() {
		return "!" + e.getUsageString();
	}
	
	@Override
	public Type getType() {
		return Types.Bool;
	}
	

}
