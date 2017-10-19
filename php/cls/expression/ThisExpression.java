package php.cls.expression;

import php.cls.AbstractPhpCls;
import php.cls.Type;

public class ThisExpression extends Expression{
	AbstractPhpCls parent;
	
	public ThisExpression(AbstractPhpCls parent) {
		if(parent == null) {
			throw new NullPointerException();
		}
		this.parent=parent;
	}
	
	@Override
	public String toString() {
		return "$this";
	}
	
	@Override
	public Type getType() {
		return parent;
	}

}
