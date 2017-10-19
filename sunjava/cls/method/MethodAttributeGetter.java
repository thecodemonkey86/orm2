package sunjava.cls.method;

import sunjava.cls.Attr;
import sunjava.cls.JavaCls;
import sunjava.cls.Method;
import util.StringUtil;

public class MethodAttributeGetter extends Method{
	Attr a;
	public MethodAttributeGetter(Attr a) {
		super(Public, a.getType(), "get"+StringUtil.ucfirst(a.getName()));
		this.a=a;
		setStatic(a.isStatic());
	}

	
	@Override
	public void addImplementation() {
		if(isStatic()) {
			_return(((JavaCls) parent).accessStaticAttribute(a));
		} else {
			_return(_this().accessAttr(a));
		}
	}

}
