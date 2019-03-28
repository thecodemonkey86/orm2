package sunjava.core.expression;

import sunjava.core.AbstractJavaCls;
import sunjava.core.Attr;
import sunjava.core.Type;
import sunjava.core.instruction.AssignInstruction;

public class AccessExpression extends Expression{

	Expression access;
	Attr attr;
	
	public AccessExpression(Expression access, Attr attr) {
		this.access = access;
		this.attr = attr;
		if (attr==null) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public Type getType() {
		return attr.getType();
	}

	
	@Override
	public String toString() {
		return access.toString() +  "." + attr;
	}

	public BinaryOperatorExpression isNotNull() {
		return new BinaryOperatorExpression(this, new NotEqualOperator(), Expressions.Null);
	}

	public AssignInstruction assign(Expression value) {
		return new AssignInstruction(this, value);
	}


	@Override
	public void collectImports(AbstractJavaCls cls) {
		access.collectImports(cls);
	}
	

}
