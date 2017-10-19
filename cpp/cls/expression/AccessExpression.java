package cpp.cls.expression;

import cpp.cls.Attr;
import cpp.cls.Type;
import cpp.cls.instruction.AssignInstruction;

public class AccessExpression extends Expression{

	Expression access;
	Attr attr;
	boolean forWrite;
	
	public AccessExpression(Expression access, Attr attr) {
		this(access, attr, false);
	}
	
	public AccessExpression(Expression access, Attr attr, boolean forWrite) {
		this.access = access;
		this.attr = attr;
		this.forWrite = forWrite;
		if (attr==null) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public Type getType() {
		return attr.getType();
	}

	@Override
	public String getReadAccessString() {
		return toString();
	}
	
	@Override
	public String toString() {
		return access.toString() + (access.getType().isPtr() ? "->" : ".") + (forWrite?attr.getWriteAccessString(): attr.getReadAccessString());
	}

	public BinaryOperatorExpression isNotNull() {
		return new BinaryOperatorExpression(this, new NotEqualOperator(), Expressions.Nullptr);
	}

	public AssignInstruction assign(Expression value) {
		return new AssignInstruction(this, value);
	}

	

}
