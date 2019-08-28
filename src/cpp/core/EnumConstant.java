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

	
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return  this.parent.getParentCls() == null ? name : this.parent.getParentCls().getName()+"::"+name;
	}

}
