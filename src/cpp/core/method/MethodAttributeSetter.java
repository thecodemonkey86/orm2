package cpp.core.method;

import util.StringUtil;
import cpp.CoreTypes;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;

public class MethodAttributeSetter extends Method{
	protected Attr attr;
	public MethodAttributeSetter(Attr a) {
		super(Public, CoreTypes.Void,getMethodName(a) );
		this.attr=a;
		addParam(new Param(a.getType().toPassParamType() , a.getName()));
		setnoexcept();
	}

	
	@Override
	public void addImplementation() {
		//_return(_this().accessAttr(a));
		addInstr(_this().accessAttr(attr).assign(getParam(attr.getName())));
	}
	
	public static String getMethodName(Attr a) {
		return "set"+StringUtil.ucfirst(a.getName());
	}

}
