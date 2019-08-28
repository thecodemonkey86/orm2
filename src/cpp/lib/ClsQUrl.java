package cpp.lib;

import cpp.CoreTypes;
import cpp.core.Cls;

public class ClsQUrl extends Cls{
	public static final String setQuery = "setQuery"; 
	public ClsQUrl() {
		super("QUrl");
		addMethod(new LibMethod(CoreTypes.Void, setQuery));
	}

}
