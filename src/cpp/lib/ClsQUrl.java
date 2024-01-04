package cpp.lib;

import cpp.CoreTypes;
import cpp.core.Cls;

public class ClsQUrl extends Cls{
	public static final String setQuery = "setQuery"; 
	public static final String toPercentEncoding = "toPercentEncoding"; 
	public ClsQUrl() {
		super("QUrl");
		addMethod(new LibMethod(CoreTypes.Void, setQuery));
		addMethod(new LibMethod(CoreTypes.QByteArray, toPercentEncoding,true));
	}

}
