package cpp.orm;

import util.pg.PgCppUtil;
import cpp.Types;
import cpp.CoreTypes;
import cpp.core.Method;
import cpp.core.MethodTemplate;
import cpp.core.QString;
import cpp.core.TplSymbol;
import cpp.core.Type;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.CStringLiteral;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.DoubleExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.Int64Expression;
import cpp.core.expression.IntExpression;
import cpp.core.expression.LongLongExpression;
import cpp.core.expression.ShortExpression;
import cpp.core.method.TplMethod;
import cpp.entityrepository.method.MethodInsertOrIgnorePg;
import cpp.lib.ClsSqlQuery;
import database.column.Column;

public class PgDatabaseTypeMapper extends DatabaseTypeMapper{
	private final class MethodTemplateInsertOrIgnorePg extends MethodTemplate {
		boolean byRef;

		private MethodTemplateInsertOrIgnorePg(String visibility, Type returnType, String name, boolean isStatic,
				boolean byRef) {
			super(visibility, returnType, name, isStatic);
			this.byRef = byRef;
			addTplType(new TplSymbol("T"));
		}

		@Override
		protected TplMethod getConcreteMethodImpl(Type... types) {
			return new MethodInsertOrIgnorePg(this, byRef,types);
		}
	}

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
			case "real":
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
				return Types.optional(Types.Int32);
			case "bigint":
				return Types.optional(Types.Int64);
			case "smallint":
				return Types.optional(Types.Int16);
			case "character varying":
			case "character":	
			case "text":
				return Types.optional(Types.QString);
			case "date":
				return Types.optional(Types.QDate);
			case "timestamp with time zone":
			case "timestamp without time zone":
				return Types.optional(Types.QDateTime);
			case "time with time zone":
				return Types.optional(Types.QTime);
			case "double precision":
			case "numeric":
			case "real":
				return Types.optional(Types.Double);
			case "bytea":
				return Types.optional(Types.QByteArray);	
			case "boolean":
				return Types.optional(CoreTypes.Bool);	
			default:
				return Types.optional(CoreTypes.QVariant);
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
					return new CStringLiteral("");
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
					return new CreateObjectExpression( Types.optional(Types.Int));
				case "bigint":
					return new CreateObjectExpression(Types.optional(Types.Int64));
				case "character varying":
				case "character":	
				case "text":
					return new CreateObjectExpression(Types.optional(Types.QString));
				case "date":
					return new CreateObjectExpression(Types.optional(Types.QDate)) ;
				case "timestamp with time zone":
				case "timestamp without time zone":
					return new CreateObjectExpression(Types.optional(Types.QDateTime)) ;
				case "time with time zone":
					return new CreateObjectExpression(Types.optional(Types.QTime) );
				case "double precision":
				case "numeric":
				case "real":
					 return new CreateObjectExpression(Types.optional(Types.Double));
				case "bytea":
					return new CreateObjectExpression(Types.optional(Types.QByteArray)) ;	
				case "boolean":
					return new CreateObjectExpression(Types.optional(CoreTypes.Bool));
				default:
					return new CreateObjectExpression(Types.optional(CoreTypes.QVariant)) ;
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
		} else if(!col.isNullable())
		{
			switch(col.getDbType()) {
			case "integer":
				return new IntExpression(Integer.valueOf(col.getDefaultValue()));
			case "bigint":
				return new LongLongExpression(Long.valueOf(col.getDefaultValue()));
			case "smallint":
				return new ShortExpression(Short.valueOf(col.getDefaultValue()));
			case "character varying":
			case "character":	
			case "text":
				return QString.fromStringConstant(col.getDefaultValue());
			case "double precision":
			case "numeric":
			case "real":
				return new DoubleExpression(Double.valueOf(col.getDefaultValue()));
			case "boolean":
				return col.getDefaultValue().equals("true") ||col.getDefaultValue().equals("1") ?  BoolExpression.TRUE : BoolExpression.FALSE;
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
		return Types.PgSqlQuery;
	}

	@Override
	public MethodTemplate getInsertOrIgnoreMethod(boolean byref) {
		return new MethodTemplateInsertOrIgnorePg(Method.Public, Types.Void, "insertOrIgnore", true, byref); 
	}

//	@Override
//	public String getRepositoryInsertOrIgnoreMethod() {
//		return ClsBaseRepository.insertOrIgnorePg;
//	}
//
//	@Override
//	public String getRepositoryPrepareInsertOrIgnoreMethod() {
//		return ClsBaseRepository.prepareInsertOrIgnorePg;
//	}
}
