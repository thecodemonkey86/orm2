package cpp.cls.expression;

import generate.CodeUtil2;
import cpp.cls.Type;

public class NewOperator extends Expression {

	protected Type type;
	protected Expression[] args;
	
	public NewOperator(Type type, Expression...args) {
		this.type = type;
		this.args = args;
	}
	
	@Override
	public String toString() {
		return CodeUtil2.sp("new",type.getConstructorName()+CodeUtil2.parentheses(CodeUtil2.commaSep((Object[])args)));
	}
	
	@Override
	public Type getType() {
		return type;
	}

}
