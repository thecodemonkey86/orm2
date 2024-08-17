package cpp.core.expression;

import codegen.CodeUtil;
import cpp.core.Cls;
import cpp.core.UniquePtr;

public class MakeUniqueExpression extends Expression {
	protected Expression[] args;
	protected UniquePtr type;
	
	public MakeUniqueExpression(Cls type, Expression...args) {
		this.args = args;
		this.type = type.toUniquePointer();
	}
	
	@Override
	public UniquePtr getType() {
		return type;
	}

	@Override
	public String toString() {
		return "std::make_unique"+CodeUtil.abr(type.getElementType().toDeclarationString())+ CodeUtil.parentheses(CodeUtil.commaSep((Object[])args));
	}
}
