package sunjava.core.expression;

import sunjava.core.AbstractJavaCls;
import sunjava.core.Type;
import sunjava.core.Types;

public class NotExpression extends Expression{
	protected Expression e;
	
	
	public NotExpression(Expression e) {
		super();
		this.e = e;
	}

	@Override
	public String toString() {
		return "!" + e.toString();
	}
	
	@Override
	public Type getType() {
		return Types.Bool;
	}
	
	@Override
	public void collectImports(AbstractJavaCls cls) {
		e.collectImports(cls);
	}

}
