package cpp.lib;

import cpp.CoreTypes;
import cpp.core.Cls;

public class ClsQUrlQuery extends Cls{

	public static final String addQueryItem = "addQueryItem"; 

	
	public ClsQUrlQuery() {
		super("QUrlQuery");
		addMethod(new LibMethod(CoreTypes.Void, addQueryItem));
	}

}
