package cpp.orm;

import cpp.CoreTypes;
import cpp.core.Type;
import cpp.core.expression.Expression;
import cpp.lib.ClsQJsonValue;

public class JsonOrmUtil {
	public static Expression jsonConvertMethod( Expression e, Type target) {
		if(target.equals(CoreTypes.Int) || target.equals(CoreTypes.Int32)) {
			return e.callMethod(ClsQJsonValue.toInt);
		} else if(target.equals(CoreTypes.QString)) {
			return e.callMethod(ClsQJsonValue.toString);
		}
		
		return e;
	}
}
