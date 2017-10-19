package cpp.cls.expression;

import generate.CodeUtil2;
import cpp.cls.Type;

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
