package cpp.core.expression;

import codegen.CodeUtil;
import cpp.core.NonMemberMethod;
import cpp.core.Type;

public class NonMemberMethodCallExpression extends Expression {

	NonMemberMethod method;
	Expression[] args;
	
	public NonMemberMethodCallExpression(NonMemberMethod  method, Expression ... args) {
		this.method = method;
		this.args = args;
	}
	
	@Override
	public Type getType() {
		return method.getReturnType();
	}

	@Override
	public String toString() {
		return "::"+method.getName()+CodeUtil.parentheses(CodeUtil.commaSep((Object[])args));
	}

}
