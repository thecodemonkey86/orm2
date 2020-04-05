package cpp.core.method;

import cpp.core.Attr;

public class MethodStaticAttributeGetter extends MethodAttributeGetter {

	public MethodStaticAttributeGetter(Attr a) {
		super(a);
		setStatic(true);
	}
	
	@Override
	public void addImplementation() {
		_return(parent.getStaticAttribute(a.getName()));
	}

}
