package php.cls.expression;

import codegen.CodeUtil;
import php.Types;
import php.cls.AbstractPhpCls;
import php.cls.PrimitiveType;
import php.cls.Type;

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
