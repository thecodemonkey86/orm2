package cpp.core.expression;

import cpp.core.Cls;
import cpp.core.Type;

public class ThisExpression extends Expression{
	protected Cls parent;
	
	public ThisExpression(Cls parent) {
		this.parent=parent;
	}
	
	@Override
	public String toString() {
		return "this";
	}
	
	@Override
	public Type getType() {
		return parent.toRawPointer();
	}
}
