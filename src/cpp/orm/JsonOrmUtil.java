package cpp.orm;

import cpp.CoreTypes;
import cpp.Types;
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
			return Types.QDate.callStaticMethod(ClsQDate.fromString,  e.callMethod(ClsQJsonValue.toString), QString.fromStringConstant("yyyy-MM-dd"));
		} else if(target.equals(CoreTypes.QDateTime)) {
			return Types.QDateTime.callStaticMethod(ClsQDateTime.fromString,  e.callMethod(ClsQJsonValue.toString), QString.fromStringConstant("yyyy-MM-dd HH:ii:ss"));
		} else if(target.equals(CoreTypes.UInt16)  || target.equals(CoreTypes.UInt8)  ) {
			return new StaticCast(target, e.callMethod(ClsQJsonValue.toInt)) ;
		} else if(target.equals(CoreTypes.UInt) || target.equals(CoreTypes.UInt32)  || target.equals(CoreTypes.UInt64)) {
			return new StaticCast(target, e.callMethod(ClsQJsonValue.toDouble)) ;
		}
		
		return e;
	}

	public static Expression convertToString(Expression expression) {
		if(expression.getType().equals(CoreTypes.Int32) ||expression.getType().equals(CoreTypes.Int16) ||expression.getType().equals(CoreTypes.Int8)||expression.getType().equals(CoreTypes.Int64) ||
				expression.getType().equals(CoreTypes.UInt32) ||expression.getType().equals(CoreTypes.UInt16) ||expression.getType().equals(CoreTypes.UInt8)||expression.getType().equals(CoreTypes.UInt64)) {
			return CoreTypes.QString.callStaticMethod(ClsQString.number,expression);
		} else if(expression.getType().equals(Types.QDate)) {
			return expression.callMethod(ClsQDate.toString, QString.fromStringConstant("yyyy-MM-dd"));
		} else if(expression.getType().equals(Types.QDateTime)) {
			return expression.callMethod(ClsQDateTime.toString, QString.fromStringConstant("yyyy-MM-dd HH:ii:ss"));
		}
		return expression;
	}
}
