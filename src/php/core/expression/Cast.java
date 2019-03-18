package php.core.expression;

import codegen.CodeUtil;
import php.core.Type;

public class Cast extends Expression{
	protected Type castType;
	protected Expression expression;
	
	public Cast(Type castType, Expression expression) {
		this.castType = castType;
		this.expression = expression;
	}

	@Override
	public Type getType() {
		return this.castType;
	}

	@Override
	public String toString() {
		return CodeUtil.parentheses(CodeUtil.parentheses(castType.toDeclarationString()) + expression);
	}

}
