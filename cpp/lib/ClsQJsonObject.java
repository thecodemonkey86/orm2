package cpp.lib;

import cpp.JsonTypes;
import cpp.core.Cls;

public class ClsQJsonObject extends Cls{

	public static String value = "value";
	
	public ClsQJsonObject() {
		super("QJsonObject");
		addMethod(new LibMethod(JsonTypes.QJsonValue, value));
	}

}
