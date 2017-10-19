package php.cls.expression;

import codegen.CodeUtil;
import php.cls.AbstractPhpCls;
import php.cls.PhpCls;
import php.cls.Method;
import php.cls.Type;

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
