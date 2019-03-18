package php.core.expression;

import codegen.CodeUtil;
import php.core.PhpCls;
import php.core.Type;
import php.core.method.Method;

public class StaticMethodCall extends Expression {

	protected PhpCls cls;
	protected Method method;
	protected Expression[] args;
	
	public StaticMethodCall(PhpCls cls, Method method, Expression...args) {
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
