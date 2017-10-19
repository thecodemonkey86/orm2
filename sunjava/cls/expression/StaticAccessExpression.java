package sunjava.cls.expression;

import sunjava.cls.AbstractJavaCls;
import sunjava.cls.Attr;
import sunjava.cls.JavaCls;
import sunjava.cls.Type;

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
