package cpp.cls.expression;

import cpp.cls.Cls;
import cpp.cls.Type;

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
