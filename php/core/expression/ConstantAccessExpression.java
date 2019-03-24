package php.core.expression;

import php.core.PhpCls;
import php.core.ClassConstant;
import php.core.Type;

public class ConstantAccessExpression extends Expression{

	protected PhpCls cls;
	protected ClassConstant c;
	
	public ConstantAccessExpression(PhpCls cls, ClassConstant c) {
		this.cls = cls;
		this.c = c;
	}
	
	@Override
	public Type getType() {
		return c.getType();
	}

	@Override
	public String toString() {
		return (cls==null ? "self" : cls.toString())+"::"+c.getName();
	}

}
