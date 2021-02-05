package cpp.core.method;

import cpp.core.Attr;

public class MethodStaticAttributeGetter extends MethodAttributeGetter {

	public MethodStaticAttributeGetter(Attr a) {
		super(a);
		setStatic(true);
	}
	
	public MethodStaticAttributeGetter(Attr a,boolean returnConstRef) {
		super(a);
		setStatic(true);
		if(returnConstRef) {
			setReturnType(getReturnType().toConstRef());
		}
	}
	
	@Override
	public void addImplementation() {
		_return(parent.getStaticAttribute(a.getName()));
	}

}
