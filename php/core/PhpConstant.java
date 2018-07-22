package php.core;

import php.core.expression.Var;

public class PhpConstant extends Var{
	

	public PhpConstant(Type type, String name) {
		super(type, name);
	}

	@Override
	public Type getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	
}
