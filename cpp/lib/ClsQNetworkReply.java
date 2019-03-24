package cpp.lib;

import cpp.CoreTypes;
import cpp.core.Cls;

public class ClsQNetworkReply extends Cls{

	
	
	public static final String deleteLater = "deleteLater";
	public static final String readAll = "readAll";

	public ClsQNetworkReply() {
		super("QNetworkReply");
		addMethod(new LibMethod(CoreTypes.Void, deleteLater));
		addMethod(new LibMethod(CoreTypes.QByteArray, readAll));
	}

}
