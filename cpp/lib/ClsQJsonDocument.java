package cpp.lib;

import cpp.JsonTypes;
import cpp.core.Cls;

public class ClsQJsonDocument extends Cls{

	public static final String fromJson = "fromJson";
	public static final String object = "object";
	public static final String array = "array";

	public ClsQJsonDocument() {
		super("QJsonDocument");
		addMethod(new LibMethod(this, fromJson, true));
		addMethod(new LibMethod(JsonTypes.QJsonObject, object));
		addMethod(new LibMethod(JsonTypes.QJsonArray, array));
	}

}
