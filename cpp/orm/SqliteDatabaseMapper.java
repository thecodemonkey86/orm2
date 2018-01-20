package cpp.orm;

import cpp.Types;
import cpp.CoreTypes;
import cpp.core.Method;
import cpp.core.QString;
import cpp.core.Type;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.DoubleExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.IntExpression;
import cpp.core.expression.LongLongExpression;
import cpp.core.expression.ShortExpression;
import database.column.Column;

public class SqliteDatabaseMapper extends DatabaseTypeMapper {
	@Override
	public Method getQVariantConvertMethod(String dbType) {
		switch (dbType) {
		case "INT":
		case "INTEGER":
		case "MEDIUMINT":
			return CoreTypes.QVariant.getTemplateMethod("value", Types.Int32);
		case "BIGINT":
			return CoreTypes.QVariant.getTemplateMethod("value", Types.Int64);
		case "SMALLINT":
			return CoreTypes.QVariant.getTemplateMethod("value", Types.Int16);
		case "CHARACTER":
		case "VARCHAR":
		case "VARYING CHARACTER":
		case "NCHAR":
		case "NATIVE CHARACTER":
		case "NVARCHAR":
		case "TEXT":
			return CoreTypes.QVariant.getMethod("toString");
		case "DATE":
			return CoreTypes.QVariant.getMethod("toDate");
		case "DATETIME":
		case "TIMESTAMP":
			return CoreTypes.QVariant.getMethod("toDateTime");
		case "NUMERIC":
		case "DECIMAL":
		 case "REAL":
		 case "DOUBLE":
		 case "DOUBLE PRECISION":
		 case "FLOAT":
			return CoreTypes.QVariant.getMethod("toDouble");
		case "BLOB":
			return CoreTypes.QVariant.getMethod("toByteArray");
		case "BOOLEAN":
			return CoreTypes.QVariant.getMethod("toBool");
		default:
			throw new RuntimeException("type" + dbType + " not implemented");
		}
	}

	@Override
	public Type getTypeFromDbDataType(String dbType, boolean nullable) {
		/*
		 * public static $intTypes = ['INT', 'INTEGER', 'TINYINT', 'SMALLINT',
		 * 'MEDIUMINT', 'BIGINT', 'BIG INT', 'INT2', 'INT8']; public static $textTypes =
		 * ['CHARACTER', 'VARCHAR', 'VARYING CHARACTER', 'NCHAR', 'NATIVE CHARACTER',
		 * 'NVARCHAR', 'TEXT', 'BLOB', 'BINARY']; public static $numericTypes =
		 * ['NUMERIC', 'DECIMAL', 'BOOLEAN', 'DATE', 'DATETIME', 'TIMESTAMP']; public
		 * static $blobTypes = ['BLOB', 'NONE']; public static $realTypes = ['REAL',
		 * 'DOUBLE', 'DOUBLE PRECISION', 'FLOAT'];
		 */

		if (!nullable) {
			switch (dbType) {
			case "INT":
			case "INTEGER":
			case "MEDIUMINT":
				return Types.Int32;
			case "TINYINT":
				return Types.Int8;
			case "SMALLINT":
				return Types.Int16;

			case "BIGINT":
				return Types.Int64;
			case "CHARACTER":
			 case "VARCHAR":
			 case "VARYING CHARACTER":
			 case "NCHAR":
			 case "NATIVE CHARACTER":
			 case "NVARCHAR":
			 case "TEXT":
				 return Types.QString;
			 case "DATETIME":
			 case "TIMESTAMP":
				return Types.QDateTime;
			 case "NUMERIC":
			 case "DECIMAL":
			 case "REAL":
			 case "DOUBLE":
			 case "DOUBLE PRECISION":
			 case "FLOAT":
				return Types.Double;
			 case "BLOB":
				 return Types.QByteArray;
			 case "BOOLEAN":
				return Types.Bool;
			default:
				return CoreTypes.QVariant;

			}
		} else {
			switch (dbType) {
			case "INT":
			case "INTEGER":
			case "MEDIUMINT":
				return Types.nullable(Types.Int32);
			case "TINYINT":
				return Types.nullable(Types.Int8);
			case "SMALLINT":
				return Types.nullable(Types.Int16);

			case "BIGINT":
				return Types.nullable(Types.Int64);
			case "CHARACTER":
			 case "VARCHAR":
			 case "VARYING CHARACTER":
			 case "NCHAR":
			 case "NATIVE CHARACTER":
			 case "NVARCHAR":
			 case "TEXT":
				 return Types.nullable(Types.QString);
			 case "DATETIME":
			 case "TIMESTAMP":
				return Types.nullable(Types.QDateTime);
			 case "NUMERIC":
			 case "DECIMAL":
			 case "REAL":
			 case "DOUBLE":
			 case "DOUBLE PRECISION":
			 case "FLOAT":
				return Types.nullable(Types.Double);
			 case "BLOB":
				 return Types.nullable(Types.QByteArray);
			 case "BOOLEAN":
					return Types.nullable(Types.Bool);
			default:
				return Types.nullable(CoreTypes.QVariant);

			}
		}

	}

	@Override
	public Expression getColumnDefaultValueExpression(Column col) {
		/*case "integer":
					return new IntExpression(0);
				case "bigint":
					return new LongLongExpression(0L);
				case "smallint":
					return new ShortExpression((short)0);
				case "character varying":
				case "character":	
				case "text":
					return QString.fromStringConstant("");
				case "date":
					return new CreateObjectExpression(Types.QDate) ;
				case "double precision":
				case "numeric":
					return new DoubleExpression(0.0);
				case "bytea":
					return new CreateObjectExpression(Types.QByteArray) ;	
				case "boolean":
					return BoolExpression.FALSE;
				case "timestamp with time zone":
					return new CreateObjectExpression(Types.QDateTime) ;
				case "time with time zone":
					return new CreateObjectExpression(Types.QTime) ;
				default:
					return new CreateObjectExpression(CoreTypes.QVariant) ;*/
		
		String dbType = col.getDbType();
		switch (dbType) {
		case "INT":
		case "INTEGER":
		case "MEDIUMINT":
			return new IntExpression(0);
		case "BIGINT":
			return new LongLongExpression(0L);
		case "SMALLINT":
			return new ShortExpression((short)0);
		case "CHARACTER":
		case "VARCHAR":
		case "VARYING CHARACTER":
		case "NCHAR":
		case "NATIVE CHARACTER":
		case "NVARCHAR":
		case "TEXT":
			return QString.fromStringConstant("");
		case "DATE":
			return new CreateObjectExpression(Types.QDate) ;
		case "DATETIME":
		case "TIMESTAMP":
			return new CreateObjectExpression(Types.QDateTime) ;
		case "NUMERIC":
		case "DECIMAL":
		 case "REAL":
		 case "DOUBLE":
		 case "DOUBLE PRECISION":
		 case "FLOAT":
			 return new DoubleExpression(0.0);
		case "BLOB":
			return new CreateObjectExpression(Types.QByteArray) ;	
		case "BOOLEAN":
			return BoolExpression.FALSE;
		default:
			throw new RuntimeException("type" + dbType + " not implemented");
		}
	}

	@Override
	public Expression getGenericDefaultValueExpression(Column col) {
		String string = col.getDefaultValue();
		if (string == null) {
			Type type = getTypeFromDbDataType(col.getDbType(), col.isNullable());
			if (!col.isNullable()) {
				if (type.equals(Types.Int)) {
					return new IntExpression(0);
				} else if (type.equals(Types.Double)) {
					return new DoubleExpression(0.0);
				}
			}
		}
		return null;
	}

	@Override
	public Type columnToType(Column col) {
		return getTypeFromDbDataType(col.getDbType(), col.isNullable());
	}
}
