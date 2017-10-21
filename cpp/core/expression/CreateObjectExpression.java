package cpp.core.expression;

import cpp.core.Cls;
import cpp.core.Type;
import generate.CodeUtil2;

public class CreateObjectExpression extends Expression{

	protected Type type;
	protected Expression[] args;
	
	public CreateObjectExpression(Type type, Expression...args) {
		if (type == null || !(type instanceof Cls)) {
			throw new IllegalArgumentException();
		}
		this.type = type;
		this.args = args;
	}
	
	@Override
	public String toString() {
		return CodeUtil2.sp(type.toUsageString()+CodeUtil2.parentheses(CodeUtil2.commaSep((Object[])args)));
	}
	
	@Override
	public Type getType() {
		return type;
	}

}
