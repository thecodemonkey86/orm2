package sunjava.cls.expression;

import sunjava.cls.AbstractJavaCls;
import sunjava.cls.Type;

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
