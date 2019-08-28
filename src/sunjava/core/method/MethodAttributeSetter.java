package sunjava.core.method;

import sunjava.core.Attr;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
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
