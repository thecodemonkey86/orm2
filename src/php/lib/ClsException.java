package php.lib;

import php.core.Types;

public class ClsException extends PhpLibCls {

	public static String getMessage = "getMessage";
	
	public ClsException() {
		super("Exception");
		addMethod(new LibMethod(Types.String, getMessage)); 
	}
	
	
	public ClsException(String name, String pkg) {
		super(name, pkg);
		addMethod(new LibMethod(Types.String, getMessage)); 
	}
	
	

}
