package sunjava.core.expression;

import sunjava.core.AbstractJavaCls;
import sunjava.core.Type;
import util.CodeUtil2;

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
	
	@Override
	public void collectImports(AbstractJavaCls cls) {
		condition.collectImports(cls);
		ifExpression.collectImports(cls);
		elseExpression.collectImports(cls);
	}


}
