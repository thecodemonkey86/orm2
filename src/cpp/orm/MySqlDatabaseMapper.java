package cpp.orm;

import cpp.Types;
import cpp.core.Method;
import cpp.core.MethodTemplate;
import cpp.core.QString;
import cpp.core.Type;
import cpp.CoreTypes;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.DoubleExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.Int16Expression;
import cpp.core.expression.Int64Expression;
import cpp.core.expression.Int8Expression;
import cpp.core.expression.IntExpression;
import cpp.core.expression.UInt16Expression;
import cpp.core.expression.UInt64Expression;
import cpp.core.expression.UInt8Expression;
import cpp.core.expression.UIntExpression;
import cpp.lib.ClsQVariant;
import cpp.lib.ClsSqlQuery;
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
			return CoreTypes.QVariant.getTemplateMethod(ClsQVariant.value, Types.Int16);
		case "tinyint":
			return CoreTypes.QVariant.getTemplateMethod(ClsQVariant.value, Types.Int8);
		case "int_unsigned":
			return CoreTypes.QVariant.getTemplateMethod(ClsQVariant.value, Types.UInt32);
		case "bigint_unsigned":
			return CoreTypes.QVariant.getTemplateMethod(ClsQVariant.value, Types.UInt64);
		case "smallint_unsigned":
			return CoreTypes.QVariant.getTemplateMethod(ClsQVariant.value, Types.UInt16);
		case "tinyint_unsigned":
			return CoreTypes.QVariant.getTemplateMethod(ClsQVariant.value, Types.UInt8);
		case "varchar":
		case "character":	
		case "char":	
		case "text":
			return CoreTypes.QVariant.getMethod("toString");
		case "date":
			return CoreTypes.QVariant.getMethod("toDate");
		case "timestamp":
		case "datetime":
			return CoreTypes.QVariant.getMethod("toDateTime");
		case "double precision":
		case "decimal":
			return CoreTypes.QVariant.getMethod("toDouble");
		case "blob":
		case "mediumblob":
		case "longblob":
		case "tinyblob":
		case "binary":
		case "varbinary":
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
				case "int_unsigned":
					return Types.UInt32;
				case "bigint_unsigned":
					return Types.UInt64;
				case "smallint_unsigned":
					return Types.UInt16;
				case "tinyint_unsigned":
					return Types.UInt8;
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
				case "blob":
				case "mediumblob":
				case "longblob":
				case "tinyblob":
				case "binary":
				case "varbinary":
					return Types.QByteArray;	
				case "boolean":
					return Types.Bool;
				case "datetime":
				case "timestamp":
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
				return new IntExpression(0);
			case "bigint":
				return new Int64Expression(0L);
			case "smallint":
				return new Int16Expression((short) 0);
			case "tinyint":
				return new Int8Expression((byte) 0);
			case "int_unsigned":
				return new UIntExpression(0);
			case "bigint_unsigned":
				return new UInt64Expression(0L);
			case "smallint_unsigned":
				return new UInt16Expression((short) 0);
			case "tinyint_unsigned":
				return new UInt8Expression((byte) 0);
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
			case "blob":
			case "mediumblob":
			case "longblob":
			case "tinyblob":
			case "binary":
			case "varbinary":
				return new CreateObjectExpression(Types.QByteArray);	
			case "boolean":
				return BoolExpression.FALSE;
			case "datetime":
			case "timestamp":
				return new CreateObjectExpression(Types.QDateTime);
			default:
				return new CreateObjectExpression(CoreTypes.QVariant);
			}
		} else {
			Expression e = getGenericDefaultValueExpression(false, dbType);
			return new CreateObjectExpression(Types.nullable(e.getType()));
		}
	}
	@Override
	public Type columnToType(Column col,boolean nullable) {
		return getTypeFromDbDataType(col.getDbType(),nullable);
	}

	@Override
	public ClsSqlQuery getSqlQueryType() {
		return Types.MySqlQuery;
	}

	@Override
	public MethodTemplate getInsertOrIgnoreMethod(boolean byref) {
		throw new RuntimeException("not impl");
	}

	 
}
