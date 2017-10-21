package cpp.core;

import codegen.CodeUtil;
import cpp.core.expression.Expression;

public class MethodCall extends Expression {

	protected Expression expression;
	protected Method method;
	protected Expression[] args;
	
	public MethodCall(Expression expression, Method method, Expression ...args) {
		this.expression = expression;
		this.args = args;
		this.method = method;
		if (method==null) {
			throw new NullPointerException();
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
			strArgs[i] = args[i].getReadAccessString();
		}
		
		return expression + (expression.getType().isPtr() ? "\n->" : ".") +method.getName()+CodeUtil.parentheses(CodeUtil.commaSep(strArgs));
	}

	
	
	

}
