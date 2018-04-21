package cpp.core;

import cpp.core.expression.Expression;

public class EnumConstant extends Expression {

	Enum parent;
	String name;
	
	public EnumConstant(Enum parent, String name) {
		this.parent = parent;
		this.name = name;
	}
	
	@Override
	public Type getType() {
		return this.parent;
	}

	@Override
	public String toString() {
		return this.parent.getParentCls().getName()+"::"+name;
	}

}
