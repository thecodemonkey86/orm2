package sunjava.cls.expression;

import sunjava.cls.AbstractJavaCls;
import sunjava.cls.Attr;
import sunjava.cls.Type;
import sunjava.cls.instruction.AssignInstruction;

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
