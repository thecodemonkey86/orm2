package php.orm;

import database.column.Column;
import php.bean.BeanCls;
import php.core.PhpFunctions;
import php.core.Type;
import php.core.Types;
import php.core.expression.DoubleExpression;
import php.core.expression.Expression;
import php.core.expression.Expressions;
import php.core.expression.InlineIfExpression;
import php.core.expression.IntExpression;
import php.core.expression.NewOperator;
import php.core.expression.PhpStringLiteral;

public class FirebirdDatabaseTypeMapper extends DatabaseTypeMapper{
	
	
	
	@Override
	public Type getTypeFromDbDataType(String dbType) {
		/*
		7 = SMALLINT
		8 = INTEGER
		10 = FLOAT
		12 = DATE
		13 = TIME
		14 = CHAR
		16 = BIGINT
		27 = DOUBLE PRECISION
		35 = TIMESTAMP
		37 = VARCHAR
		261 = BLOB*/

		switch(dbType) {
		case "8":
			return Types.Int;
		case "16":
			return Types.Int;
		case "7":
			return Types.Int;
		case "14":	
		case "37":
			return Types.String;
		case "12":
			return Types.DateTime;
		case "10":
		case "27":
			return Types.Float;
		case "261":
			return Types.String;	
		case "boolean":
			return Types.Bool;
		case "35":
			return Types.DateTime;
		case "13":
			return Types.String;
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
			case "8":
			case "16":
			case "7":
				return new IntExpression(0);
			case "14":	
			case "37":
				return new PhpStringLiteral("");
			case "12":
				return new NewOperator(Types.DateTime) ;
			case "10":
			case "27":
				return new DoubleExpression(0.0);
			case "261":
				return new PhpStringLiteral("");
			case "35":
				return new NewOperator(Types.DateTime) ;
			case "13":
				return new PhpStringLiteral("");
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
		return Types.FirebirdSqlQuery;
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
		return Types.Resource;
	}

	@Override
	public Expression getDefaultFetchExpression(Expression res) {
		return PhpFunctions.ibase_fetch_assoc.call(res);
	}

	@Override
	public String filterFetchAssocArrayKey(String key) {
		return key.toUpperCase();
	}
	
	public Expression filterFetchAssocArrayKeyExpression(Expression keyExpr) {
		return PhpFunctions.strtoupper.call(keyExpr);
	}

	@Override
	public Expression getConvertTypeExpression(Expression e, String dbType, boolean nullable) {
		if(nullable) {
			return new InlineIfExpression(e.isNull(), Expressions.Null, getConvertTypeExpression(e, dbType, false));
		} else {
			switch(dbType) {
			case "8":
			case "16":
			case "7":
				return e.cast(Types.Int);
			case "14":	
			case "37":
				return e;
			case "12":
				return new NewOperator(Types.DateTime,e) ;
			case "10":
			case "27":
				return e.cast(Types.Float);
			case "261":
				return e;
			case "35":
				return new NewOperator(Types.DateTime,e) ;
			case "13":
				return e;
			default:
				return e;
			}
		}
	}
	
}