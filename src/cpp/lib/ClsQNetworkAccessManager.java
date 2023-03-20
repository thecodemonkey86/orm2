package cpp.lib;

import cpp.NetworkTypes;
import cpp.core.Cls;

public class ClsQNetworkAccessManager extends Cls{
	public static final String get = "get";
	public static final String post = "post";
	public ClsQNetworkAccessManager() {
		super("QNetworkAccessManager");
		addMethod(new LibMethod(NetworkTypes.QNetworkReply.toRawPointer(), get));
		addMethod(new LibMethod(NetworkTypes.QNetworkReply.toRawPointer(), post));
	}

}
