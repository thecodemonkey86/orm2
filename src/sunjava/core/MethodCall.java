package sunjava.core;

import codegen.CodeUtil;
import sunjava.core.expression.Expression;

public class MethodCall extends Expression {

	protected Expression expression;
	protected Method method;
	protected Expression[] args;
	
	public MethodCall(Expression expression, Method method, Expression ...args) {
		this.expression = expression;
		this.args = args;
		this.method = method;
		if (method==null) {
			System.out.println();
		}
	}
	
	@Override
	public Type getType() {
		return method.getReturnType();
	}
	
	@Override
	public String toString() {
		String[] strArgs=new String[args.length];
		for(int i=0;i<args.length;i++) {
			strArgs[i] = args[i].toString();
		}
		
		return expression + "." +method.getName()+CodeUtil.parentheses(CodeUtil.commaSep(strArgs));
	}

	@Override
	public void collectImports(AbstractJavaCls cls) {
		// TODO Auto-generated method stub
		expression.collectImports(cls);
		for(Expression a : args) {
			a.collectImports(cls);
		}
	}
	
	

}
