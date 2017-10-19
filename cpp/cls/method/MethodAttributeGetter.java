package cpp.cls.method;

import util.StringUtil;
import cpp.cls.Attr;
import cpp.cls.Method;

public class MethodAttributeGetter extends Method{
	Attr a;
	public MethodAttributeGetter(Attr a) {
		super(Public, a.getType(), "get"+StringUtil.ucfirst(a.getName()));
		this.a=a;
	}

	
	@Override
	public void addImplementation() {
		_return(_this().accessAttr(a));
	}

}
