package cpp.orm;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Type;
import cpp.core.expression.Expression;
import database.column.Column;

public class SqliteDatabaseMapper extends DatabaseTypeMapper {
	@Override
	public Method getQVariantConvertMethod(String pgType) {
		switch (pgType) {
		case "INT":
		case "INTEGER":
		case "MEDIUMINT":
			return Types.QVariant.getTemplateMethod("value", Types.Int32);
		case "BIGINT":
			return Types.QVariant.getTemplateMethod("value", Types.Int64);
		case "SMALLINT":
			return Types.QVariant.getTemplateMethod("value", Types.Int16);
		case "CHARACTER":
		case "VARCHAR":
		case "VARYING CHARACTER":
		case "NCHAR":
		case "NATIVE CHARACTER":
		case "NVARCHAR":
		case "TEXT":
			return Types.QVariant.getMethod("toString");
		case "DATE":
			return Types.QVariant.getMethod("toDate");
		case "DATETIME":
		case "TIMESTAMP":
			return Types.QVariant.getMethod("toDateTime");
		case "NUMERIC":
		case "DECIMAL":
		 case "REAL":
		 case "DOUBLE":
		 case "DOUBLE PRECISION":
		 case "FLOAT":
			return Types.QVariant.getMethod("toDouble");
		case "BLOB":
			return Types.QVariant.getMethod("toByteArray");
		case "BOOLEAN":
			return Types.QVariant.getMethod("toBool");
		default:
			throw new RuntimeException("type" + pgType + " not implemented");
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
				return Types.QVariant;

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
				return Types.nullable(Types.QVariant);

			}
		}

	}

	@Override
	public Expression getColumnDefaultValueExpression(Column col) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression getGenericDefaultValueExpression(Column col) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type columnToType(Column col) {
		return getTypeFromDbDataType(col.getDbType(), col.isNullable());
	}
}
