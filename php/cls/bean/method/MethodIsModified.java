package php.cls.bean.method;

import php.Types;
import php.cls.Attr;
import php.cls.Method;
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
