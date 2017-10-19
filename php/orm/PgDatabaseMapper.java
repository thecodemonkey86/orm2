package php.orm;

import model.Column;
import php.Types;
import php.cls.Type;
import php.cls.bean.BeanCls;
import php.cls.expression.Expression;
import php.cls.expression.Var;
import php.lib.ClsPgSqlQuery;

public class PgDatabaseMapper extends DatabaseTypeMapper{

	@Override
	public Type getTypeFromDbDataType(String dbType) {
		switch(dbType) {
		case "integer":
			return Types.Int;
		case "bigint":
			return Types.Int;
		case "smallint":
			return Types.Short;
		case "character varying":
		case "character":	
		case "text":
			return Types.String;
		case "date":
			return Types.DateTime;
		case "double precision":
		case "numeric":
			return Types.Float;
		case "bytea":
			return Types.String;	
		case "boolean":
			return Types.Bool;
		case "timestamp with time zone":
			return Types.DateTime;
		default:
			return Types.String;
		}
	}

	@Override
	public Expression getColumnDefaultValueExpression(Column col) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type columnToType(Column col) {
		// TODO Auto-generated method stub
		return getTypeFromDbDataType(col.getDbType());
	}


	@Override
	public Type getSqlQueryClass() {
		return Types.PgSqlQuery;
	}

	@Override
	public Type getBeanQueryClass(BeanCls beanCls) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getLibBeanQueryClass(BeanCls beanCls) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
