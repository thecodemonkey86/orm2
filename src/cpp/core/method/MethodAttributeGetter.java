package cpp.core.method;

import cpp.core.Attr;
import cpp.core.Method;
import util.StringUtil;

public class MethodAttributeGetter extends Method{
	Attr a;
	public MethodAttributeGetter(Attr a) {
		super(Public, a.getType(), "get"+StringUtil.ucfirst(a.getName()));
		this.a=a;
		if(!a.isStatic())
			setConstQualifier();
	}

	
	@Override
	public void addImplementation() {
		_return(_this().accessAttr(a));
	}

}
