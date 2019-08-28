package php.core.method;

import php.core.Attr;
import php.core.NullableType;
import php.core.Param;
import php.core.Types;
import util.StringUtil;

public class MethodAttributeSetter extends Method{
	protected Attr attr;
	public MethodAttributeSetter(Attr attr) {
		this(attr,false);
	}
	public MethodAttributeSetter(Attr attr, boolean removeNullable) {
		super(Public, Types.Void, "set"+StringUtil.ucfirst(attr.getName()));
		this.attr=attr;
		addParam(new Param(removeNullable && attr.getType() instanceof NullableType ? ((NullableType)attr.getType()).getElementType() : attr.getType(), attr.getName()));
	}

	
	@Override
	public void addImplementation() {
		//_return(_this().accessAttr(a));
		addInstr(_this().accessAttr(attr).assign(getParam(attr.getName())));
	}

}
