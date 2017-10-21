package php.core;

import php.core.expression.Expression;
import php.core.expression.PhpFunctionCall;

public class PhpFunction {
	String name;
	Type returnType;

	public Type getReturnType() {
		return returnType;
	}

	public String getName() {
		return name;
	}

	public PhpFunction(String name, Type returnType) {
		super();
		this.name = name;
		this.returnType = returnType;
	}
	
	public Expression call(Expression ...args ) {
		return new PhpFunctionCall(this, args);
	}
}
