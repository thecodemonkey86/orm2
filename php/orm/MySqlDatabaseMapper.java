package php.orm;

import php.cls.expression.IntExpression;
import php.cls.expression.PhpStringLiteral;
import php.cls.expression.NewOperator;
import model.Column;
import php.Types;
import php.cls.Type;
import php.cls.bean.BeanCls;
import php.cls.expression.BoolExpression;
import php.cls.expression.DoubleExpression;
import php.cls.expression.Expression;
import php.cls.expression.Expressions;
import php.cls.expression.Var;

public class MySqlDatabaseMapper extends DatabaseTypeMapper{
	
	@Override
	public Type getTypeFromDbDataType(String dbType) {
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
		if(col.isNullable()) {
			return Expressions.Null;
		} else {
			switch(col.getDbType()) {
			case "int":
			case "bigint":
			case "smallint":
				return new IntExpression(0);
			case "varchar":
			case "character":	
			case "text":
				return new PhpStringLiteral("");
			case "date":
			case "timestamp with time zone":
				return new NewOperator(Types.DateTime) ;
			case "double precision":
			case "numeric":
				return new DoubleExpression(0.0);
			case "bytea":
				return new PhpStringLiteral("");
			case "boolean":
				return BoolExpression.FALSE;
			default:
				return new PhpStringLiteral("");
			}
		}
		
	}
	

	@Override
	public Type columnToType(Column col) {
		return getTypeFromDbDataType(col.getDbType());
	}


	@Override
	public Type getSqlQueryClass() {
		return Types.MysqlSqlQuery;
	}

	@Override
	public Type getBeanQueryClass(BeanCls beanCls) {
		throw new RuntimeException("not implemented");
	}
	@Override
	public Type getLibBeanQueryClass(BeanCls beanCls) {
		throw new RuntimeException("not implemented");
	}
	
}
