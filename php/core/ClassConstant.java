package php.core;

import codegen.CodeUtil;
import php.core.expression.Expression;
import php.core.expression.Var;

public class ClassConstant extends Var{
	protected Expression value;

	public ClassConstant(Type type, String name, Expression value) {
		super(type, name);
		this.value = value;
	}

	@Override
	public Type getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	public Expression getValue() {
		return value;
	}
	
	@Override
	public String toDeclarationString() {
		return CodeUtil.sp("public","static","const",name,'=',value,';');
	}
	
	
}
