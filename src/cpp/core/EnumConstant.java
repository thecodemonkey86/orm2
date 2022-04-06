package cpp.core;

import cpp.core.expression.Expression;

public class EnumConstant extends Expression {

	Enum parent;
	String name;
	
	public EnumConstant(String name) {
		this.name = name;
	}
	
	@Override
	public Type getType() {
		return this.parent;
	}

	public void setParent(Enum parent) {
		this.parent = parent;
	}
	
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return  this.parent.getParentCls() == null ? name : this.parent.getParentCls().toDeclarationString()+"::"+name;
	}

}
