package cpp.core.expression;

import codegen.CodeUtil;
import cpp.core.SharedPtr;

public class MakeSharedExpression extends Expression {
	protected Expression[] args;
	protected SharedPtr type;
	
	public MakeSharedExpression(SharedPtr type, Expression...args) {
		this.args = args;
		this.type = type;
	}
	
	@Override
	public SharedPtr getType() {
		return type;
	}

	@Override
	public String toString() {
		return "std::make_shared"+CodeUtil.abr(type.getElementType().toDeclarationString())+ CodeUtil.parentheses(CodeUtil.commaSep((Object[])args));
	}
}
