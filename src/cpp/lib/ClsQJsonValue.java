package cpp.lib;

import cpp.CoreTypes;
import cpp.JsonTypes;
import cpp.QtCoreTypes;
import cpp.core.Cls;

public class ClsQJsonValue extends Cls {

	public static final String toInt = "toInt";
	public static final String toDouble = "toDouble";
	public static final String toString = "toString";
	public static final String toObject = "toObject";
	public static final String isNull = "isNull";
	
	public ClsQJsonValue() {
		super("QJsonValue");
		addMethod(new LibMethod(CoreTypes.Int, toInt));
		addMethod(new LibMethod(QtCoreTypes.QString, toString));
		addMethod(new LibMethod(JsonTypes.QJsonObject, toObject));
		addMethod(new LibMethod(CoreTypes.Double, toDouble));
		addMethod(new LibMethod(CoreTypes.Bool, isNull));
	}

}
