package cpp.lib;

import cpp.CoreTypes;
import cpp.core.Cls;
import cpp.core.EnumConstant;

public class ClsQNetworkRequest extends Cls{

	public static final String setAttribute = "setAttribute";
	public static final String setHeader = "setHeader";
	public final EnumConstant redirectPolicyAttribute = new EnumConstant("RedirectPolicyAttribute");
	public final EnumConstant sameOriginRedirectPolicy = new EnumConstant("SameOriginRedirectPolicy");
	public final EnumConstant contentTypeHeader = new EnumConstant("ContentTypeHeader");
	public final EnumAttribute enumAttribute = new EnumAttribute(); 
	public final EnumRedirectPolicy enumRedirectPolicy = new EnumRedirectPolicy(); 
	public final EnumKnownHeaders KnownHeaders = new EnumKnownHeaders(); 
	
	
	public ClsQNetworkRequest() {
		super("QNetworkRequest");
		addMethod(new LibMethod(CoreTypes.Void, setAttribute));
		addMethod(new LibMethod(CoreTypes.Void, setHeader));
	}
	
	private class EnumAttribute extends cpp.core.Enum {

		public EnumAttribute() {
			super(ClsQNetworkRequest.this, "Attribute",redirectPolicyAttribute );
			// TODO Auto-generated constructor stub
		}
		
	}

	private class EnumRedirectPolicy extends cpp.core.Enum {
		public EnumRedirectPolicy() {
			super(ClsQNetworkRequest.this, "RedirectPolicy",sameOriginRedirectPolicy );
		}
	}
	private class EnumKnownHeaders extends cpp.core.Enum {
		public EnumKnownHeaders() {
			super(ClsQNetworkRequest.this, "KnownHeaders",contentTypeHeader );
		}
	}
}
