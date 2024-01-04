package cpp.core.expression;

import codegen.CodeUtil;
import cpp.core.Method;
import cpp.core.Type;

public class MethodCall extends Expression {

	protected Expression expression;
	protected Method method;
	protected Expression[] args;
	
	public MethodCall(Expression expression, Method method, Expression ...args) {
		this.expression = expression;
		this.args = args;
		for(int i=0;i<args.length;i++) {
			if(args[i] == null) {
				throw new NullPointerException();
			}
		}
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
		if(expression instanceof ThisExpression) {
			if(method.isStatic()) {
				throw new RuntimeException("this in static method");
			}
		}
		return expression + (expression.getType().isPtr() ? "->" : ".") +method.getName()+CodeUtil.parentheses(CodeUtil.commaSep(strArgs));
	}

	
	
	

}
