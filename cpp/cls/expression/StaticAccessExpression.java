package cpp.cls.expression;

import cpp.cls.Attr;
import cpp.cls.Cls;
import cpp.cls.Type;

public class StaticAccessExpression extends Expression{

	protected Cls cls;
	protected Attr attr;
	
	public StaticAccessExpression(Cls cls, Attr attr) {
		this.cls = cls;
		this.attr = attr;
	}
	
	@Override
	public Type getType() {
		return attr.getType();
	}

	@Override
	public String toString() {
		return cls.toDeclarationString()+"::"+attr.getName();
	}

}
