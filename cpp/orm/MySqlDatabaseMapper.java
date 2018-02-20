package cpp.orm;

import cpp.Types;
import cpp.core.Method;
import cpp.core.QString;
import cpp.core.Type;
import cpp.CoreTypes;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.DoubleExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.IntExpression;
import cpp.lib.ClsQVariant;
import database.column.Column;

public class MySqlDatabaseMapper extends DatabaseTypeMapper{
	@Override
	public Method getQVariantConvertMethod(String pgType) {
		switch(pgType) {
		case "int":
			return CoreTypes.QVariant.getTemplateMethod(ClsQVariant.value, Types.Int32);
		case "bigint":
			return CoreTypes.QVariant.getTemplateMethod(ClsQVariant.value, Types.Int64);
		case "smallint":
			return CoreTypes.QVariant.getMethod(ClsQVariant.toShort);
		case "tinyint":
			return CoreTypes.QVariant.getTemplateMethod(ClsQVariant.value, Types.Int8);
		case "varchar":
		case "character":	
		case "char":	
		case "text":
			return CoreTypes.QVariant.getMethod("toString");
		case "date":
			return CoreTypes.QVariant.getMethod("toDate");
		case "timestamp with time zone":
			return CoreTypes.QVariant.getMethod("toDateTime");
		case "double precision":
		case "decimal":
			return CoreTypes.QVariant.getMethod("toDouble");
		case "bytea":
			return CoreTypes.QVariant.getMethod("toByteArray");
		case "boolean":
			return CoreTypes.QVariant.getMethod("toBool");
		default:
			return null;
			//throw new RuntimeException("type " + pgType+" not implemented");
		}
	}
	
	@Override
	public Type getTypeFromDbDataType(String dbType, boolean nullable) {
		if (!nullable){
			switch(dbType) {
				case "int":
					return Types.Int32;
				case "bigint":
					return Types.Int64;
				case "smallint":
					return Types.Int16;
				case "tinyint":
					return Types.Int8;
				case "varchar":
				case "character":	
				case "char":	
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
					return CoreTypes.QVariant;
				}
			} else {
				Type t = getTypeFromDbDataType(dbType, false);
				return Types.nullable(t);				
			}
	}

	@Override
	public Expression getColumnDefaultValueExpression(Column col) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Expression getGenericDefaultValueExpression(Column col) {
		return getGenericDefaultValueExpression(col.isNullable(), col.getDbType()
				);
	}
	private Expression getGenericDefaultValueExpression(boolean nullable,String dbType) {
		if (!nullable){
			switch(dbType) {
			case "int":
			case "bigint":
			case "smallint":
			case "tinyint":
				return new IntExpression(0);
			case "varchar":
			case "character":	
			case "char":	
			case "text":
				return QString.fromStringConstant("");
			case "date":
				return new CreateObjectExpression(Types.QDate);
			case "double precision":
			case "numeric":
				return new DoubleExpression(0.0);
			case "bytea":
				return new CreateObjectExpression(Types.QByteArray);	
			case "boolean":
				return BoolExpression.FALSE;
			case "timestamp with time zone":
				return new CreateObjectExpression(Types.QDateTime);
			default:
				return new CreateObjectExpression(CoreTypes.QVariant);
			}
		} else {
			Expression e = getGenericDefaultValueExpression(false, dbType);
			return new CreateObjectExpression(Types.nullable(e.getType()), e);
		}
	}
	@Override
	public Type columnToType(Column col) {
		return getTypeFromDbDataType(col.getDbType(), col.isNullable());
	}
}
