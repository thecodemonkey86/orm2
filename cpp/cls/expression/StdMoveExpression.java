package cpp.cls.expression;

import generate.CodeUtil2;
import cpp.cls.UniquePtr;

public class StdMoveExpression extends Expression {

	protected Expression ex;
	
	public StdMoveExpression(Expression ex) {
		if (! (ex.getType() instanceof UniquePtr)) {
			throw new IllegalArgumentException();
		}
		this.ex = ex;
	}
	
	@Override
	public UniquePtr getType() {
		return (UniquePtr) ex.getType();
	}

	@Override
	public String toString() {
		return "std::move"+CodeUtil2.parentheses(ex);
	}

}
