package php.core.expression;

import php.core.Type;
import php.core.Types;

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
