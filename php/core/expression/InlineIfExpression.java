package php.core.expression;

import php.core.Type;
import util.CodeUtil2;

public class InlineIfExpression extends Expression {

	protected Expression condition, ifExpression, elseExpression;
	protected Type type;
	
	
	public InlineIfExpression(Expression condition, Expression ifExpression,
			Expression elseExpression) {
		super();
		this.condition = condition;
		this.ifExpression = ifExpression;
		this.elseExpression = elseExpression;
		this.type = ifExpression.getType();
	}
	
	public InlineIfExpression(Expression condition, Expression ifExpression,
			Expression elseExpression,Type type) {
		super();
		this.condition = condition;
		this.ifExpression = ifExpression;
		this.elseExpression = elseExpression;
		this.type = type;
	}

	@Override
	public String toString() {
		return CodeUtil2.sp(condition,'?',ifExpression,':',elseExpression);
	}

	@Override
	public Type getType() {
		return type;
	}


}
