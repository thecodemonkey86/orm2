package sunjava.core.expression;

import codegen.CodeUtil;
import sunjava.core.AbstractJavaCls;
import sunjava.core.PrimitiveType;
import sunjava.core.Type;
import sunjava.core.Types;

public class JavaInstanceOfExpression extends Expression {

	protected Expression exp;
	protected Type type;
	
	public JavaInstanceOfExpression(Expression exp, Type type) {
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

	@Override
	public void collectImports(AbstractJavaCls cls) {
		type.collectImports(cls);
		exp.collectImports(cls);
	}

}
