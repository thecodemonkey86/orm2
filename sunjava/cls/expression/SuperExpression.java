package sunjava.cls.expression;

import sunjava.cls.JavaCls;
import sunjava.cls.Type;

public class SuperExpression extends Expression{
	JavaCls parent;
	
	public SuperExpression(JavaCls parent) {
		if(parent == null) {
			throw new NullPointerException();
		}
		this.parent=parent;
	}
	
	@Override
	public String toString() {
		return "super";
	}
	
	@Override
	public Type getType() {
		return parent.getSuperclass();
	}

}
