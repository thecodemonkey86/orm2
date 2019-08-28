package php.core.expression;

import codegen.CodeUtil;
import php.core.PrimitiveType;
import php.core.Type;
import php.core.Types;

public class PhpInstanceOfExpression extends Expression {

	protected Expression exp;
	protected Type type;
	
	public PhpInstanceOfExpression(Expression exp, Type type) {
		this.type = type;
		this.exp = exp;
	}

	@Override
	public PrimitiveType getType() {
		return Types.Bool;
	}

	@Override
	public String toString() {
		return CodeUtil.sp(exp, "instanceof", type.toDeclarationString());
	}


}
