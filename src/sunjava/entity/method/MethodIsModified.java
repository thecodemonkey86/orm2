package sunjava.entity.method;

import sunjava.core.Attr;
import sunjava.core.Method;
import sunjava.core.Types;
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
