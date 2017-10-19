package php.cls.expression;

import php.cls.Attr;
import php.cls.PhpCls;
import php.cls.Type;

public class StaticAccessExpression extends Expression{

	protected PhpCls cls;
	protected Attr attr;
	
	public StaticAccessExpression(PhpCls cls, Attr attr) {
		this.cls = cls;
		this.attr = attr;
	}
	
	@Override
	public Type getType() {
		return attr.getType();
	}

	@Override
	public String toString() {
		return cls.toString()+"::$"+attr.getName();
	}

}
