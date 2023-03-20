package cpp.lib;

import cpp.CoreTypes;
import cpp.core.Cls;
import cpp.core.EnumConstant;

public class ClsQNetworkReply extends Cls{

	
	
	public static final String deleteLater = "deleteLater";
	public static final String readAll = "readAll";
	public static final String error = "error";

	public final EnumConstant noError = new EnumConstant("NoError");
	public final EnumNetworkError enumNetworkError = new EnumNetworkError(); 
	
	private class EnumNetworkError extends cpp.core.Enum {

		public EnumNetworkError() {
			super(ClsQNetworkReply.this, "NetworkError",noError );
			// TODO Auto-generated constructor stub
		}
		
	}
	
	public ClsQNetworkReply() {
		super("QNetworkReply");
		addMethod(new LibMethod(CoreTypes.Void, deleteLater));
		addMethod(new LibMethod(enumNetworkError, error));
		addMethod(new LibMethod(CoreTypes.QByteArray, readAll));
	}

}
