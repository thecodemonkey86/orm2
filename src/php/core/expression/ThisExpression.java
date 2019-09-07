package php.core.expression;

import php.core.AbstractPhpCls;
import php.core.Type;

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
