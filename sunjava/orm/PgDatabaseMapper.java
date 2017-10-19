package sunjava.orm;

import model.Column;
import sunjava.Types;
import sunjava.cls.JavaString;
import sunjava.cls.Type;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.bean.repo.query.ClsBeanQuery;
import sunjava.cls.expression.BoolExpression;
import sunjava.cls.expression.Expressions;
import sunjava.cls.expression.InlineIfExpression;
import sunjava.cls.expression.NewOperator;
import sunjava.cls.expression.DoubleExpression;
import sunjava.cls.expression.Expression;
import sunjava.cls.expression.IntExpression;
import sunjava.cls.expression.LongExpression;
import sunjava.cls.expression.ShortExpression;
import sunjava.cls.expression.Var;
import sunjava.lib.ClsBaseBeanQuery;
import sunjava.lib.ClsJavaString;
import sunjava.lib.ClsLocalDate;
import sunjava.lib.ClsResultSet;
import sunjava.lib.ClsSqlDate;
import sunjava.lib.ClsTimestamp;
import sunjava.lib.ClsZoneId;
import sunjava.lib.ClsZonedDateTime;

public class PgDatabaseMapper extends DatabaseTypeMapper{
	
	@Override
	public Expression getResultSetValueGetter(Var resultSet,Column col,String alias){
		return getResultSetValueGetterInternal(resultSet, col, JavaString.stringConstant( alias+ "__" +col.getName()));
	}
	
	@Override
	public Expression getResultSetValueGetter(Var resultSet, Column col,
			Expression alias) {
		return getResultSetValueGetterInternal(resultSet, col, Types.String.callStaticMethod(ClsJavaString.format, JavaString.stringConstant("%s__%s"), alias, JavaString.stringConstant(col.getName())));
	}
	
	@Override
	protected Expression getResultSetValueGetterInternal(Var resultSet,Column col,Expression identifer){
		String dbType = col.getDbType();
		
		switch(dbType) {
			case "integer":
				return resultSet.callMethod(ClsResultSet.METHOD_NAME_GET_INT,identifer);
			case "bigint":
				return resultSet.callMethod(ClsResultSet.METHOD_NAME_GET_LONG,identifer);
			case "smallint":
				return resultSet.callMethod(ClsResultSet.METHOD_NAME_GET_SHORT,identifer);
			case "character varying":
			case "character":	
			case "text":
				return resultSet.callMethod(ClsResultSet.METHOD_NAME_GET_STRING,identifer);
			case "date":
				//return ZonedDateTime.ofInstant(resultSet.callMethod( ClsResultSet.METHOD_NAME_GET_TIMESTAMP("zoneddatetime")).toInstant(), ZoneId.systemDefault()) Types.ZonedDateTime;
				
				/*Date date = resultSet.getDate("dateonly"); 
				System.out.println(date.toLocalDate());*/
				
				return new InlineIfExpression(resultSet.callMethod(ClsResultSet.METHOD_NAME_GET_DATE, identifer).isNull(), Expressions.Null, resultSet.callMethod(ClsResultSet.METHOD_NAME_GET_DATE, identifer).callMethod(ClsSqlDate.METHOD_NAME_TO_LOCAL_DATE)) ;
				
			case "double precision":
			case "numeric":
				return resultSet.callMethod(ClsResultSet.METHOD_NAME_GET_DOUBLE,identifer);
			case "bytea":
				return resultSet.callMethod(ClsResultSet.METHOD_NAME_GET_BYTES,identifer);	
			case "boolean":
				return resultSet.callMethod(ClsResultSet.METHOD_NAME_GET_BOOLEAN,identifer);
			case "timestamp with time zone":
				return new InlineIfExpression(resultSet.callMethod(ClsResultSet.METHOD_NAME_GET_TIMESTAMP, identifer).isNull(), Expressions.Null, Types.ZonedDateTime.callStaticMethod(ClsZonedDateTime.METHOD_NAME_OF_INSTANT, resultSet.callMethod(ClsResultSet.METHOD_NAME_GET_TIMESTAMP, identifer).callMethod(ClsTimestamp.METHOD_NAME_TO_INSTANT),new ClsZoneId().callStaticMethod(ClsZoneId.METHOD_NAME_SYSTEM_DEFAULT))) ;
			default:
				return resultSet.callMethod(ClsResultSet.METHOD_NAME_GET_OBJECT,identifer);
			}
	}
	
	@Override
	public Type getTypeFromDbDataType(String dbType, boolean nullable){
		if (!nullable){
		switch(dbType) {
			case "integer":
				return Types.Int;
			case "bigint":
				return Types.Long;
			case "smallint":
				return Types.Short;
			case "character varying":
			case "character":	
			case "text":
				return Types.String;
			case "date":
				return Types.LocalDate;
			case "double precision":
			case "numeric":
				return Types.Double;
			case "bytea":
				return Types.ByteArray;	
			case "boolean":
				return Types.Bool;
			case "timestamp with time zone":
				return Types.ZonedDateTime;
			default:
				return Types.Object;
			}
		} else {
			switch(dbType) {
			case "integer":
				return Types.nullable(Types.Int);
			case "bigint":
				return Types.nullable(Types.Long);
			case "smallint":
				return Types.nullable(Types.Short);
			case "character varying":
			case "character":	
			case "text":
				return Types.nullable(Types.String);
			case "date":
				return Types.nullable(Types.LocalDate);
			case "timestamp with time zone":
				return Types.nullable(Types.ZonedDateTime);
			case "double precision":
			case "numeric":
				return Types.nullable(Types.Double);
			case "bytea":
				return Types.nullable(Types.ByteArray);	
			default:
				return Types.nullable(Types.Object);
			}
		}
	}

	private Expression getDefaultValueExpression(Column col) {
		boolean nullable = col.isNullable();
		String dbType = col.getDbType();
		if (!nullable){
			switch(dbType) {
				case "integer":
					return new IntExpression(0);
				case "bigint":
					return new LongExpression(0L);
				case "smallint":
					return new ShortExpression((short)0);
				case "character varying":
				case "character":	
				case "text":
					return JavaString.stringConstant("");
				case "date":
					return Types.LocalDate.callStaticMethod(ClsLocalDate.METHOD_NAME_NOW) ;
				case "double precision":
				case "numeric":
					return new DoubleExpression(0.0);
				case "bytea":
					return new NewOperator(Types.ByteArray) ;	
				case "boolean":
					return BoolExpression.FALSE;
				case "timestamp with time zone":
					return Types.ZonedDateTime.callStaticMethod(ClsZonedDateTime.METHOD_NAME_NOW) ;
				default:
					return new NewOperator(Types.Object) ;
				}
			} else {
				/*switch(dbType) {
				case "integer":
					return Expressions.Null;
				case "bigint":
					return Expressions.Null;
				case "character varying":
				case "character":	
				case "text":
					return Expressions.Null;
				case "date":
					return Expressions.Null;
				case "timestamp with time zone":
					return Expressions.Null;
				case "double precision":
				case "numeric":
					return Expressions.Null;
				case "bytea":
					return new NewOperator(Types.ByteArray) ;		
				default:
					return new NewOperator(Types.Object) ;
				}*/
				return Expressions.Null;
			}
	}
	
	/**
	 * 
	 * @param col get default value for this column
	 * @param databaseDefaultValueString default value as defined in database. May be a function like nextval (PG: serial)
	 * @return expression, that represents a default value for that column database type
	 */
	@Override
	public Expression getDefaultValueExpression(Column col,String databaseDefaultValueString) {
		
		if (databaseDefaultValueString == null) {
			Type type = getTypeFromDbDataType(col.getDbType(), col.isNullable());
			if (!col.isNullable()) {
				if (type.equals(Types.Int)) {
					return new IntExpression(0);
				} else if (type.equals(Types.Double)) {
					return new DoubleExpression(0.0);
				}
			}
		} else if (databaseDefaultValueString.startsWith("nextval(")) {
			return new IntExpression(-1);
		}
		return getDefaultValueExpression(col);
	}

	@Override
	public Type columnToType(Column col) {
		return getTypeFromDbDataType(col.getDbType(), col.isNullable());
	}

	@Override
	public Type getSqlQueryClass() {
		return Types.PgSqlQuery;
	}

	@Override
	public Type getBeanQueryClass(BeanCls beanCls) {
		return new ClsBeanQuery(beanCls);
	}
	
	@Override
	public Type getLibBeanQueryClass(BeanCls beanCls) {
		return new ClsBaseBeanQuery(beanCls);
	}

}
