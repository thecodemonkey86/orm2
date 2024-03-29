package cpp.orm;

import cpp.Types;
import cpp.core.Method;
import cpp.core.MethodTemplate;
import cpp.core.QString;
import cpp.core.TplSymbol;
import cpp.core.Type;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.DoubleExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.IntExpression;
import cpp.core.expression.LongLongExpression;
import cpp.core.expression.ShortExpression;
import cpp.core.method.TplMethod;
import cpp.entityrepository.method.MethodInsertOrIgnoreSqlite;
import cpp.lib.ClsSqlQuery;
import database.column.Column;

public class SqliteDatabaseMapper extends DatabaseTypeMapper {
	private final class MethodTemplateInsertOrIgnoreSqlite extends MethodTemplate {
		boolean byRef;

		private MethodTemplateInsertOrIgnoreSqlite(String visibility, Type returnType, String name, boolean isStatic,
				boolean byRef) {
			super(visibility, returnType, name, isStatic);
			this.byRef = byRef;
			addTplType(new TplSymbol("T"));
		}

		@Override
		protected TplMethod getConcreteMethodImpl(Type... types) {
			return new MethodInsertOrIgnoreSqlite(this,byRef, types);
		}
	}

	@Override
	public Method getQVariantConvertMethod(String dbType) {
		switch (dbType.toUpperCase()) {
		case "INT":
		case "INTEGER":
		case "MEDIUMINT":
			return Types.QVariant.getTemplateMethod("value", Types.Int32);
		case "BIGINT":
			return Types.QVariant.getTemplateMethod("value", Types.Int64);
		case "SMALLINT":
			return Types.QVariant.getTemplateMethod("value", Types.Int16);
		case "TINYINT":
			return Types.QVariant.getTemplateMethod("value", Types.Int8);
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
			switch (dbType.toUpperCase()) {
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
			 case "DATE":
				 return Types.QDate;
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
			return Types.nullable(getTypeFromDbDataType(dbType, false));
		}

	}
	
	private Expression getColumnDefaultValueExpressionImpl(String dbType, boolean nullable) {
		if (!nullable) {
			switch (dbType.toUpperCase()) {
				case "INT":
				case "INTEGER":
				case "MEDIUMINT":
					return new IntExpression(0);
				case "BIGINT":
					return new LongLongExpression(0L);
				case "SMALLINT":
				case "TINYINT":
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
					throw new RuntimeException("type " + dbType + " not implemented");
				}
		} else {
			Expression e = getColumnDefaultValueExpressionImpl(dbType, false);
			return new CreateObjectExpression(Types.nullable(e.getType()));
		}
	}

	@Override
	public Expression getColumnDefaultValueExpression(Column col) {
		String dbType = col.getDbType();
		return getColumnDefaultValueExpressionImpl(dbType, col.isNullable());
	}

	@Deprecated
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
	public Type columnToType(Column col,boolean nullable) {
		return getTypeFromDbDataType(col.getDbType(),nullable);
	}
	
	@Override
	public ClsSqlQuery getSqlQueryType() {
		return Types.SqliteSqlQuery;
	}

	@Override
	public MethodTemplate getInsertOrIgnoreMethod(boolean byRef) {
return new MethodTemplateInsertOrIgnoreSqlite(Method.Public, Types.Void, "insertOrIgnore", true, byRef); 
	}

	 
}
