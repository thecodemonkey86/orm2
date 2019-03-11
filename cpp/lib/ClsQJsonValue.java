package cpp.lib;

import cpp.CoreTypes;
import cpp.QtCoreTypes;
import cpp.core.Cls;

public class ClsQJsonValue extends Cls {

	public static final String toInt = "toInt";
	public static final String toString = "toString";
	public static final String isNull = "isNull";
	
	public ClsQJsonValue() {
		super("QJsonValue");
		addMethod(new LibMethod(CoreTypes.Int, toInt));
		addMethod(new LibMethod(QtCoreTypes.QString, toString));
		addMethod(new LibMethod(CoreTypes.Bool, isNull));
	}

}
