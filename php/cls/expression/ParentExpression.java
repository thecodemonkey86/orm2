package php.cls.expression;

import php.cls.PhpCls;
import php.cls.Type;

public class ParentExpression extends Expression{
	PhpCls parent;
	
	public ParentExpression(PhpCls parent) {
		if(parent == null) {
			throw new NullPointerException();
		}
		this.parent=parent;
	}
	
	@Override
	public String toString() {
		return "parent";
	}
	
	@Override
	public Type getType() {
		return parent.getSuperclass();
	}

}
