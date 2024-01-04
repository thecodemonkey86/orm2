package sunjava.core.expression;

import codegen.CodeUtil;
import sunjava.core.AbstractJavaCls;
import sunjava.core.JavaCls;
import sunjava.core.Method;
import sunjava.core.Type;

public class StaticMethodCall extends Expression {

	protected JavaCls cls;
	protected Method method;
	protected Expression[] args;
	
	public StaticMethodCall(JavaCls cls, Method method, Expression...args) {
		this.cls = cls;
		this.method = method;
		this.args = args;
	}
	
	@Override
	public Type getType() {
		return method.getReturnType();
	}

	@Override
	public String toString() {
		return cls.toStaticMethodCallString()+"."+method.getName()+CodeUtil.parentheses(CodeUtil.commaSep((Object[])args));
	}

	@Override
	public void collectImports(AbstractJavaCls cls) {
		this.cls.collectImports(cls);
		for(Expression a : args) 
			a.collectImports(cls);
	}

}
