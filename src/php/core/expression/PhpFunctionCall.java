package php.core.expression;

import codegen.CodeUtil;
import php.core.PhpFunction;
import php.core.Type;

public class PhpFunctionCall extends Expression{

	private PhpFunction function;
	private Expression[] args;
	
	
	
	public PhpFunctionCall(PhpFunction function, Expression... args) {
		super();
		this.function = function;
		this.args = args;
	}

	@Override
	public Type getType() {
		return function.getReturnType();
	}

	@Override
	public String toString() {
		String[] args = new String[this.args.length];
		for(int i = 0; i < args.length; i++) {
			args[i] = this.args[i].getUsageString();
		}
		return function.getName()+ CodeUtil.parentheses(CodeUtil.commaSep(args));
	}

}
