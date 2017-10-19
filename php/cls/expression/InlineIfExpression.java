package php.cls.expression;

import generate.CodeUtil2;
import php.cls.AbstractPhpCls;
import php.cls.Type;

public class InlineIfExpression extends Expression {

	protected Expression condition, ifExpression, elseExpression;
	
	
	
	public InlineIfExpression(Expression condition, Expression ifExpression,
			Expression elseExpression) {
		super();
		this.condition = condition;
		this.ifExpression = ifExpression;
		this.elseExpression = elseExpression;
	}

	@Override
	public String toString() {
		return CodeUtil2.sp(condition,'?',ifExpression,':',elseExpression);
	}

	@Override
	public Type getType() {
		return ifExpression.getType();
	}


}
