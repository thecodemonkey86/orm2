package cpp.core.expression;

import codegen.CodeUtil;
import cpp.core.Type;

public class StaticCast extends Expression {
	protected Type castType;
	protected Expression expression;
	
	public StaticCast(Type castType, Expression expression) {
		this.castType = castType;
		this.expression = expression;
	}

	@Override
	public Type getType() {
		return this.castType;
	}

	@Override
	public String toString() {
		return "static_cast"+ CodeUtil.abr(castType.toDeclarationString())+ CodeUtil.parentheses( expression);
	}


}
