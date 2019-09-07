package php.lib;

import php.core.PhpCls;

public class PhpLibCls extends PhpCls{

	
	public PhpLibCls(String name) {
		this(name,"\\");
	}
	public PhpLibCls(String name, String namespace) {
		super(name, namespace);
	}

}
