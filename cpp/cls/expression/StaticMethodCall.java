package cpp.cls.expression;

import codegen.CodeUtil;
import cpp.cls.Cls;
import cpp.cls.Method;
import cpp.cls.Type;

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
