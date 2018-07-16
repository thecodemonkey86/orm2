package php.lib;

import php.core.Types;

public class ClsDateTime extends PhpLibCls{

	public static final String format = "format";
	
	public ClsDateTime() {
		super("DateTime");
		addMethod(new LibMethod(Types.String, format));
	}

}
