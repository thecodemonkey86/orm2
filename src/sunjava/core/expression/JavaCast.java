package sunjava.core.expression;

import codegen.CodeUtil;
import sunjava.core.AbstractJavaCls;
import sunjava.core.Type;

public class JavaCast extends Expression {

	protected Type castType;
	protected Expression expression;
	
	public JavaCast(Type castType, Expression expression) {
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


	@Override
	public void collectImports(AbstractJavaCls cls) {
		castType.collectImports(cls);
		expression.collectImports(cls);
	}
}
