package sunjava.core.expression;

import sunjava.core.AbstractJavaCls;
import sunjava.core.Attr;
import sunjava.core.JavaCls;
import sunjava.core.Type;

public class StaticAccessExpression extends Expression{

	protected JavaCls cls;
	protected Attr attr;
	
	public StaticAccessExpression(JavaCls cls, Attr attr) {
		this.cls = cls;
		this.attr = attr;
	}
	
	@Override
	public Type getType() {
		return attr.getType();
	}

	@Override
	public String toString() {
		return cls.toString()+"."+attr.getName();
	}

	@Override
	public void collectImports(AbstractJavaCls cls) {
		this.cls.collectImports(cls);
	}
}
