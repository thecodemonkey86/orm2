package php.orm;

import database.column.Column;
import php.bean.BeanCls;
import php.core.Type;
import php.core.Types;
import php.core.expression.BoolExpression;
import php.core.expression.DoubleExpression;
import php.core.expression.Expression;
import php.core.expression.Expressions;
import php.core.expression.IntExpression;
import php.core.expression.NewOperator;
import php.core.expression.PhpStringLiteral;
import php.lib.ClsMysqliResult;

public class MySqlDatabaseTypeMapper extends DatabaseTypeMapper{
	
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

	@Override
	public Type getDatabaseLinkType() {
		return Types.mysqli;
	}

	@Override
	public Expression getDefaultFetchExpression(Expression res) {
		return res.callMethod(ClsMysqliResult.fetch_assoc);
	}

	@Override
	public Expression getConvertTypeExpression(Expression e,String dbType, boolean nullable) {
		// TODO Auto-generated method stub
		return e;
	}

	
}
