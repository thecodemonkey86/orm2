package cpp.core.expression;

import cpp.core.UniquePtr;
import generate.CodeUtil2;

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
