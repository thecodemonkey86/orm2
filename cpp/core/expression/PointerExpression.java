package cpp.core.expression;

import cpp.core.Type;

public class PointerExpression extends Expression{

	Var var;
	
	public PointerExpression(Var var) {
		this.var = var;
	}

	@Override
	public Type getType() {
		return var.getType().toRawPointer();
	}

	@Override
	public String toString() {
		return "&"+var.toString();
	}

}
