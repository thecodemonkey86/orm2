package sunjava.cls.method;

import sunjava.Types;
import sunjava.cls.Attr;
import sunjava.cls.Method;
import sunjava.cls.Param;
import util.StringUtil;

public class MethodAttributeSetter extends Method{
	protected Attr attr;
	public MethodAttributeSetter(Attr attr) {
		super(Public, Types.Void, "set"+StringUtil.ucfirst(attr.getName()));
		this.attr=attr;
		addParam(new Param(attr.getType(), attr.getName()));
	}

	
	@Override
	public void addImplementation() {
		//_return(_this().accessAttr(a));
		addInstr(_this().accessAttr(attr).assign(getParam(attr.getName())));
	}

}
