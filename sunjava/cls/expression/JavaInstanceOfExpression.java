package sunjava.cls.expression;

import codegen.CodeUtil;
import sunjava.Types;
import sunjava.cls.AbstractJavaCls;
import sunjava.cls.PrimitiveType;
import sunjava.cls.Type;

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
