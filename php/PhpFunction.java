package php;

import php.cls.Type;
import php.cls.expression.Expression;
import php.cls.expression.PhpFunctionCall;

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
