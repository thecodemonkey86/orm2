package sunjava.cls.bean.method;

import sunjava.Types;
import sunjava.cls.Attr;
import sunjava.cls.Method;
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
