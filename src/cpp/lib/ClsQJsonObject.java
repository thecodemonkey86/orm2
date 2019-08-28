package cpp.lib;

import cpp.CoreTypes;
import cpp.JsonTypes;
import cpp.core.Cls;

public class ClsQJsonObject extends Cls{

	public static final String value = "value";
	public static final String contains = "contains";
	
	public ClsQJsonObject() {
		super("QJsonObject");
		addMethod(new LibMethod(JsonTypes.QJsonValue, value));
		addMethod(new LibMethod(CoreTypes.Bool, contains));
	}

}
