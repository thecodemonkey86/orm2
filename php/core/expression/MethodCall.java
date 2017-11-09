package php.core.expression;

import codegen.CodeUtil;
import php.core.Type;
import php.core.method.Method;

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
			strArgs[i] = args[i].getUsageString();
		}
		
		return expression.getUsageString() + "->" +method.getName()+CodeUtil.parentheses(CodeUtil.commaSep(strArgs));
	}

	
	

}
