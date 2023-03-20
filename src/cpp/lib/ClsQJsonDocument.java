package cpp.lib;

import cpp.CoreTypes;
import cpp.JsonTypes;
import cpp.core.Cls;
import cpp.core.EnumConstant;

public class ClsQJsonDocument extends Cls{

	public static final String fromJson = "fromJson";
	public static final String toJson = "toJson";
	public static final String object = "object";
	public static final String setObject = "setObject";
	public static final String array = "array";
	public final EnumConstant Compact=new EnumConstant("Compact");  
	public final EnumJsonFormat enumJsonFormat = new EnumJsonFormat();
	public ClsQJsonDocument() {
		super("QJsonDocument");
		addMethod(new LibMethod(this, fromJson, true));
		addMethod(new LibMethod(CoreTypes.QByteArray, toJson));
		addMethod(new LibMethod(JsonTypes.QJsonObject, object));
		addMethod(new LibMethod(CoreTypes.Void, setObject));
		addMethod(new LibMethod(JsonTypes.QJsonArray, array));
	}
	
	private class EnumJsonFormat extends cpp.core.Enum {

		public EnumJsonFormat() {
			super(ClsQJsonDocument.this, "JsonFormat", Compact);
		}
		
	}

}
