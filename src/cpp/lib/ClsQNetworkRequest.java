package cpp.lib;

import cpp.CoreTypes;
import cpp.core.Cls;
import cpp.core.EnumConstant;

public class ClsQNetworkRequest extends Cls{

	public static final String setAttribute = "setAttribute";
	public final EnumConstant FollowRedirectsAttribute = new EnumConstant("FollowRedirectsAttribute");
	public final EnumAttribute enumAttribute = new EnumAttribute(); 
	
	
	public ClsQNetworkRequest() {
		super("QNetworkRequest");
		addMethod(new LibMethod(CoreTypes.Void, setAttribute));
	}
	
	private class EnumAttribute extends cpp.core.Enum {

		public EnumAttribute() {
			super(ClsQNetworkRequest.this, "Attribute",FollowRedirectsAttribute );
			// TODO Auto-generated constructor stub
		}
		
	}

}
