package sunjava.core.expression;

import sunjava.core.AbstractJavaCls;
import sunjava.core.Type;

public class ThisExpression extends Expression{
	AbstractJavaCls parent;
	
	public ThisExpression(AbstractJavaCls parent) {
		if(parent == null) {
			throw new NullPointerException();
		}
		this.parent=parent;
	}
	
	@Override
	public String toString() {
		return "this";
	}
	
	@Override
	public Type getType() {
		return parent;
	}

}
