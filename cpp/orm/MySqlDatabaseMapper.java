package cpp.orm;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Type;
import cpp.core.expression.Expression;
import database.column.Column;

public class MySqlDatabaseMapper extends DatabaseTypeMapper{
	@Override
	public Method getQVariantConvertMethod(String pgType) {
		switch(pgType) {
		case "int":
		case "bigint":
			return Types.QVariant.getMethod("toInt");
		case "smallint":
			return Types.QVariant.getMethod("toInt");
		case "varchar":
		case "character":	
		case "text":
			return Types.QVariant.getMethod("toString");
		case "date":
			return Types.QVariant.getMethod("toDate");
		case "timestamp with time zone":
			return Types.QVariant.getMethod("toDateTime");
		case "double precision":
		case "decimal":
			return Types.QVariant.getMethod("toDouble");
		case "bytea":
			return Types.QVariant.getMethod("toByteArray");
		case "boolean":
			return Types.QVariant.getMethod("toBool");
		default:
			throw new RuntimeException("type" + pgType+" not implemented");
		}
	}
	
	@Override
	public Type getTypeFromDbDataType(String dbType, boolean nullable) {
		if (!nullable){
			switch(dbType) {
				case "int":
				case "bigint":
					return Types.Int;
				case "smallint":
					return Types.Int;
				case "varchar":
				case "character":	
				case "text":
					return Types.QString;
				case "date":
					return Types.QDate;
				case "double precision":
				case "numeric":
					return Types.Double;
				case "bytea":
					return Types.QByteArray;	
				case "boolean":
					return Types.Bool;
				case "timestamp with time zone":
					return Types.QDateTime;
				default:
					return Types.QVariant;
				}
			} else {
				switch(dbType) {
				case "int":
				case "bigint":
					return Types.nullable(Types.Int);
				case "character varying":
				case "character":	
				case "text":
					return Types.nullable(Types.QString);
				case "date":
					return Types.nullable(Types.QDate);
				case "timestamp with time zone":
					return Types.nullable(Types.QDateTime);
				case "double precision":
				case "numeric":
					return Types.nullable(Types.Double);
				case "bytea":
					return Types.nullable(Types.QByteArray);	
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
