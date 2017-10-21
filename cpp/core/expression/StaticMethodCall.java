package cpp.core.expression;

import codegen.CodeUtil;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Type;

public class StaticMethodCall extends Expression {

	protected Cls cls;
	protected Method method;
	protected Expression[] args;
	
	public StaticMethodCall(Cls cls, Method method, Expression...args) {
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
		return cls.toDeclarationString()+"::"+method.getName()+CodeUtil.parentheses(CodeUtil.commaSep((Object[])args));
	}

}
