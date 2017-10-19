package sunjava.cls.expression;

import sunjava.Types;
import sunjava.cls.AbstractJavaCls;
import sunjava.cls.Type;

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
