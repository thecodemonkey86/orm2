package cpp.orm;

import cpp.CoreTypes;
import cpp.Types;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.Type;
import cpp.core.expression.Expression;
import cpp.core.expression.StaticCast;
import cpp.lib.ClsQDate;
import cpp.lib.ClsQDateTime;
import cpp.lib.ClsQJsonValue;
import cpp.lib.ClsQString;

public class JsonOrmUtil {
	public static Expression jsonConvertMethod( Expression e, Type target) {
		if(target.equals(CoreTypes.Int) || target.equals(CoreTypes.Int32)) {
			return e.callMethod(ClsQJsonValue.toInt);
		} else if(target.equals(CoreTypes.Short) || target.equals(CoreTypes.Int16)) {
			return new StaticCast(CoreTypes.Short, e.callMethod(ClsQJsonValue.toInt)) ;
		} else if(target.equals(CoreTypes.Int8)) {
			return new StaticCast(CoreTypes.Int8, e.callMethod(ClsQJsonValue.toInt)) ;
		} else if(target.equals(CoreTypes.QString)) {
			return e.callMethod(ClsQJsonValue.toString);
		} else if(target.equals(CoreTypes.QDate)) {
			return Types.QDate.callStaticMethod(ClsQDate.fromString,  e.callMethod(ClsQJsonValue.toString, QString.fromStringConstant("yyyy-MM-dd")));
		} else if(target.equals(CoreTypes.QDateTime)) {
			return Types.QDateTime.callStaticMethod(ClsQDateTime.fromString,  e.callMethod(ClsQJsonValue.toString, QString.fromStringConstant("yyyy-MM-dd HH:ii:ss")));
		}
		
		return e;
	}

	public static Expression convertToString(Param param) {
		if(param.getType().equals(CoreTypes.Int32) ||param.getType().equals(CoreTypes.Int16) ||param.getType().equals(CoreTypes.Int8)||param.getType().equals(CoreTypes.Int64)) {
			return CoreTypes.QString.callStaticMethod(ClsQString.number,param);
		} else if(param.getType().equals(Types.QDate)) {
			return param.callMethod(ClsQDate.toString, QString.fromStringConstant("yyyy-MM-dd"));
		} else if(param.getType().equals(Types.QDateTime)) {
			return param.callMethod(ClsQDateTime.toString, QString.fromStringConstant("yyyy-MM-dd HH:ii:ss"));
		}
		return param;
	}
}
