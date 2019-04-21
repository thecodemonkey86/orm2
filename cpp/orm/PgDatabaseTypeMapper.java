package cpp.orm;

import util.pg.PgCppUtil;
import cpp.Types;
import cpp.CoreTypes;
import cpp.core.Method;
import cpp.core.QString;
import cpp.core.Type;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.DoubleExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.Int64Expression;
import cpp.core.expression.IntExpression;
import cpp.core.expression.LongLongExpression;
import cpp.core.expression.ShortExpression;
import database.column.Column;

public class PgDatabaseTypeMapper extends DatabaseTypeMapper{
	@SuppressWarnings("deprecation")
	@Override
	public Method getQVariantConvertMethod(String pgType) {
		return PgCppUtil.pgToQVariantConvertMethod(pgType);
	}
	
	@Override
	public Type getTypeFromDbDataType(String dbType, boolean nullable){
		if (!nullable){
		switch(dbType) {
			case "integer":
				return Types.Int32;
			case "bigint":
				return Types.Int64;
			case "smallint":
				return Types.Int16;
			case "character varying":
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
			case "timestamp without time zone":
				return Types.QDateTime;
			case "time with time zone":
				return Types.QTime;
			default:
				return CoreTypes.QVariant;
			}
		} else {
			switch(dbType) {
			case "integer":
				return Types.nullable(Types.Int32);
			case "bigint":
				return Types.nullable(Types.Int64);
			case "smallint":
				return Types.nullable(Types.Int16);
			case "character varying":
			case "character":	
			case "text":
				return Types.nullable(Types.QString);
			case "date":
				return Types.nullable(Types.QDate);
			case "timestamp with time zone":
			case "timestamp without time zone":
				return Types.nullable(Types.QDateTime);
			case "time with time zone":
				return Types.nullable(Types.QTime);
			case "double precision":
			case "numeric":
				return Types.nullable(Types.Double);
			case "bytea":
				return Types.nullable(Types.QByteArray);	
			default:
				return Types.nullable(CoreTypes.QVariant);
			}
		}
	}

	@Override
	public Expression getGenericDefaultValueExpression(Column col) {
		boolean nullable = col.isNullable();
		String dbType = col.getDbType();
		if (!nullable){
			switch(dbType) {
				case "integer":
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
				case "timestamp without time zone":
					return new CreateObjectExpression(Types.QDateTime) ;
				case "time with time zone":
					return new CreateObjectExpression(Types.QTime) ;
				default:
					return new CreateObjectExpression(CoreTypes.QVariant) ;
				}
			} else {
				switch(dbType) {
				case "integer":
					return new CreateObjectExpression( Types.nullable(Types.Int));
				case "bigint":
					return new CreateObjectExpression(Types.nullable(Types.LongLong));
				case "character varying":
				case "character":	
				case "text":
					return new CreateObjectExpression(Types.nullable(Types.QString));
				case "date":
					return new CreateObjectExpression(Types.nullable(Types.QDate)) ;
				case "timestamp with time zone":
				case "timestamp without time zone":
					return new CreateObjectExpression(Types.nullable(Types.QDateTime)) ;
				case "time with time zone":
					return new CreateObjectExpression(Types.nullable(Types.QTime) );
				case "double precision":
				case "numeric":
					 return new CreateObjectExpression(Types.nullable(Types.Double));
				case "bytea":
					return new CreateObjectExpression(Types.nullable(Types.QByteArray)) ;		
				default:
					return new CreateObjectExpression(Types.nullable(CoreTypes.QVariant)) ;
				}
			}
	}
	
	@Override
	public Expression getColumnDefaultValueExpression(Column col) {
		String string = col.getDefaultValue();
		if (string == null) {
			Type type = getTypeFromDbDataType(col.getDbType(), col.isNullable());
			if (!col.isNullable()) {
				if (type.equals(Types.Int)) {
					return new IntExpression(0);
				} else if (type.equals(Types.Double)) {
					return new DoubleExpression(0.0);
				} else if (type.equals(Types.Int64)) {
					return new Int64Expression(0L);
				}
			}
		} else if (string.startsWith("nextval(")) {
			return null;
		}
		return null;
	}

	@Override
	public Type columnToType(Column col) {
		return getTypeFromDbDataType(col.getDbType(), col.isNullable());
	}
}
