package cpp.cls.method;

import util.StringUtil;
import cpp.Types;
import cpp.cls.Attr;
import cpp.cls.Method;
import cpp.cls.Param;

public class MethodAttributeSetter extends Method{
	protected Attr attr;
	public MethodAttributeSetter(Attr a) {
		super(Public, Types.Void, "set"+StringUtil.ucfirst(a.getName()));
		this.attr=a;
		addParam(new Param(a.getType(), a.getName()));
	}

	
	@Override
	public void addImplementation() {
		//_return(_this().accessAttr(a));
		addInstr(_this().accessAttr(attr).assign(getParam(attr.getName())));
	}

}
