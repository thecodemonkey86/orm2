package sunjava.orm;

import model.Column;
import sunjava.Types;
import sunjava.cls.Type;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.expression.Expression;
import sunjava.cls.expression.Var;

public class MySqlDatabaseMapper extends DatabaseTypeMapper{
	
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
					return Types.String;
				case "date":
					return Types.ZonedDateTime;
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
				case "int":
				case "bigint":
					return Types.nullable(Types.Int);
				case "character varying":
				case "character":	
				case "text":
					return Types.nullable(Types.String);
				case "date":
					return Types.nullable(Types.ZonedDateTime);
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

	@Override
	public Expression getDefaultValueExpression(Column col,String str) {
		throw new RuntimeException("not implemented");
	}
	

	@Override
	public Type columnToType(Column col) {
		return getTypeFromDbDataType(col.getDbType(), col.isNullable());
	}

	@Override
	public Expression getResultSetValueGetter(Var resultSet, Column col,
			Expression alias){
		throw new RuntimeException("not implemented");
	}

	@Override
	public Type getSqlQueryClass() {
		throw new RuntimeException("not implemented");
	}

	@Override
	public Type getBeanQueryClass(BeanCls beanCls) {
		throw new RuntimeException("not implemented");
	}
	@Override
	public Type getLibBeanQueryClass(BeanCls beanCls) {
		throw new RuntimeException("not implemented");
	}

	@Override
	public Expression getResultSetValueGetter(Var resultSet, Column col,
			String alias) {
		throw new RuntimeException("not implemented");
	}

	@Override
	protected Expression getResultSetValueGetterInternal(Var resultSet,
			Column col, Expression identifier) {
		throw new RuntimeException("not implemented");
	}
	
}
