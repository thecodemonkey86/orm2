package cpp.orm;

import cpp.Types;
import cpp.core.Method;
import cpp.core.QString;
import cpp.core.Type;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.DoubleExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.Expressions;
import cpp.core.expression.IntExpression;
import cpp.core.expression.LongLongExpression;
import cpp.core.expression.ShortExpression;
import database.column.Column;

public class FirebirdDatabaseTypeMapper extends DatabaseTypeMapper {

	@Override
	public Method getQVariantConvertMethod(String dbType) {
		switch(dbType) {
		case "8":
			return Types.QVariant.getMethod("toInt");
		case "16":
			return Types.QVariant.getMethod("toLongLong");
		case "7":
			return Types.QVariant.getMethod("toInt");
		case "14":	
		case "37":
			return Types.QVariant.getMethod("toString");
		case "12":
			return Types.QVariant.getMethod("toDate");
		case "35":
			return Types.QVariant.getMethod("toDateTime");
		case "13":
			return Types.QVariant.getMethod("toTime");
		case "10":
		case "27":
			return Types.QVariant.getMethod("toDouble");
		case "261":
			return Types.QVariant.getMethod("toByteArray");
		default:
			throw new RuntimeException("type " + dbType+" not implemented");
		}
	}

	@Override
	public Type getTypeFromDbDataType(String dbType, boolean nullable) {
/*
7 = SMALLINT
8 = INTEGER
10 = FLOAT
12 = DATE
13 = TIME
14 = CHAR
16 = BIGINT
27 = DOUBLE PRECISION
35 = TIMESTAMP
37 = VARCHAR
261 = BLOB*/
		if (!nullable){
			switch(dbType) {
				case "8":
					return Types.Int;
				case "16":
					return Types.LongLong;
				case "7":
					return Types.Short;
				case "14":	
				case "37":
					return Types.QString;
				case "12":
					return Types.QDate;
				case "10":
				case "27":
					return Types.Double;
				case "261":
					return Types.QByteArray;	
				case "boolean":
					return Types.Bool;
				case "35":
					return Types.QDateTime;
				case "13":
					return Types.QTime;
				default:
					return Types.QVariant;
				}
			} else {
				switch(dbType) {
				case "8":
					return Types.nullable(Types.Int);
				case "16":
					return Types.nullable(Types.LongLong);
				case "7":
					return Types.nullable(Types.Short);
				case "14":	
				case "37":
					return Types.nullable(Types.QString);
				case "12":
					return Types.nullable(Types.QDate);
				case "35":
					return Types.nullable(Types.QDateTime);
				case "13":
					return Types.nullable(Types.QTime);
				case "10":
				case "27":
					return Types.nullable(Types.Double);
				case "261":
					return Types.nullable(Types.QByteArray);	
				default:
					return Types.nullable(Types.QVariant);
				}
			}
	}

	@Override
	public Expression getColumnDefaultValueExpression(Column col) {
		Type type = getTypeFromDbDataType(col.getDbType(), col.isNullable());
		if (!col.isNullable()) {
			if (type.equals(Types.Int)) {
				return new IntExpression(0);
			} else if (type.equals(Types.Double)) {
				return new DoubleExpression(0.0);
			}
		}
		return Expressions.Nullptr;
	}

	@Override
	public Expression getGenericDefaultValueExpression(Column col) {
		boolean nullable = col.isNullable();
		String dbType = col.getDbType();
		if (!nullable){
			switch(dbType) {
				case "8":
					return new IntExpression(0);
				case "16":
					return new LongLongExpression(0L);
				case "7":
					return new ShortExpression((short)0);
				case "14":	
				case "37":
					return QString.fromStringConstant("");
				case "12":
					return new CreateObjectExpression(Types.QDate) ;
				case "10":
				case "27":
					return new DoubleExpression(0.0);
				case "261":
					return new CreateObjectExpression(Types.QByteArray) ;	
				case "35":
					return new CreateObjectExpression(Types.QDateTime) ;
				case "13":
					return new CreateObjectExpression(Types.QTime) ;
				default:
					return new CreateObjectExpression(Types.QVariant) ;
				}
			} else {
				switch(dbType) {
				case "8":
					IntExpression intExpression = new IntExpression(0);
					return new CreateObjectExpression( Types.nullable(intExpression.getType()),intExpression);
				case "16":
					LongLongExpression longLongExpression = new LongLongExpression(0L);
					return new CreateObjectExpression(Types.nullable(Types.LongLong), longLongExpression);
				case "14":	
				case "37":
					return new CreateObjectExpression(Types.nullable(Types.QString));
				case "12":
					return new CreateObjectExpression(Types.nullable(Types.QDate)) ;
				case "35":
					return new CreateObjectExpression(Types.nullable(Types.QDateTime)) ;
				case "13":
					return new CreateObjectExpression(Types.nullable(Types.QTime) );
				case "10":
				case "27":
					 DoubleExpression doubleExpression = new DoubleExpression(0.0);
					 return new CreateObjectExpression(Types.nullable(doubleExpression.getType()), doubleExpression);
				case "261":
					return new CreateObjectExpression(Types.nullable(Types.QByteArray)) ;		
				default:
					return new CreateObjectExpression(Types.nullable(Types.QVariant)) ;
				}
			}
	}

	@Override
	public Type columnToType(Column col) {
		return getTypeFromDbDataType(col.getDbType(), col.isNullable());
	}

}
