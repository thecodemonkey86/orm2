package php.cls.method;

import php.Types;
import php.cls.Attr;
import php.cls.Method;
import php.cls.Param;
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
