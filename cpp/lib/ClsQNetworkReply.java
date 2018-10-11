package cpp.lib;

import cpp.CoreTypes;
import cpp.core.Cls;

public class ClsQNetworkReply extends Cls{

	
	
	public static final String deleteLater = "deleteLater";

	public ClsQNetworkReply() {
		super("QNetworkRequest");
		addMethod(new LibMethod(CoreTypes.Void, deleteLater));
	}

}
