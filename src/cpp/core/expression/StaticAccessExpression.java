package cpp.core.expression;

import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.Type;

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
