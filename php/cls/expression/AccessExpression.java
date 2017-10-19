package php.cls.expression;

import php.cls.Attr;
import php.cls.Type;
import php.cls.instruction.AssignInstruction;

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
		return access.toString() +  "->" + attr.getName();
	}

	public BinaryOperatorExpression isNotNull() {
		return new BinaryOperatorExpression(this, new NotEqualOperator(), Expressions.Null);
	}

	public AssignInstruction assign(Expression value) {
		return new AssignInstruction(this, value);
	}


}
