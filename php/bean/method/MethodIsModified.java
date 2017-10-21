package php.bean.method;

import php.core.Attr;
import php.core.Types;
import php.core.method.Method;
import util.StringUtil;

public class MethodIsModified extends Method{

	protected Attr a;
	
	public MethodIsModified(Attr a) {
		super(Public, Types.Bool, "is" + StringUtil.ucfirst( a.getName()) );
		this.a = a;
	}

	@Override
	public void addImplementation() {
		_return(a);
		
	}

}
