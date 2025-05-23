package php.orm;

import database.column.Column;
import php.core.Attr;
import php.core.PhpConstants;
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
import php.core.method.Method;
import php.entity.EntityCls;
import php.entityrepository.method.FirebirdEntityRepositoryBeginTransactionMethod;
import php.entityrepository.method.FirebirdEntityRepositoryCommitTransactionMethod;
import php.entityrepository.method.FirebirdEntityRepositoryRollbackTransactionMethod;
import php.lib.ClsDateTime;

public class FirebirdDatabaseTypeMapper extends DatabaseTypeMapper{
	
	
	
	@Override
	public Type getTypeFromDbDataType(String dbType,boolean nullable) {
		if(dbType==null) {
			throw new IllegalArgumentException();
		}
		if(nullable) {
			return getTypeFromDbDataType(dbType, false).toNullable();
		}
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
				return new NewOperator(Types.DateTime, new PhpStringLiteral("now"),new NewOperator(Types.DateTimeZone,new PhpStringLiteral("Europe/Berlin"))) ;
			case "10":
			case "27":
				return new DoubleExpression(0.0);
			case "261":
				return new PhpStringLiteral("");
			case "35":
				return new NewOperator(Types.DateTime, new PhpStringLiteral("now"),new NewOperator(Types.DateTimeZone,new PhpStringLiteral("Europe/Berlin"))) ;
			case "13":
				return new PhpStringLiteral("");
			default:
				return new PhpStringLiteral("");
			}
			
		}
		
	}
	

	@Override
	public Type columnToType(Column col) {
		return getTypeFromDbDataType(col);
	}


	@Override
	public Type getSqlQueryClass() {
		return Types.FirebirdSqlQuery;
	}

	@Override
	public Type getBeanQueryClass(EntityCls beanCls) {
		throw new RuntimeException("not implemented");
	}
	@Override
	public Type getLibBeanQueryClass(EntityCls beanCls) {
		throw new RuntimeException("not implemented");
	}

	@Override
	public Type getDatabaseLinkType() {
		return Types.Resource;
	}

	@Override
	public Expression getDefaultFetchExpression(Expression res) {
		return PhpFunctions.ibase_fetch_assoc.call(res,PhpConstants.IBASE_TEXT);
	}

	@Override
	public String filterFetchAssocArrayKey(String key) {
		return key.toUpperCase();
	}
	
	public Expression filterFetchAssocArrayKeyExpression(Expression keyExpr) {
		return PhpFunctions.strtoupper.call(keyExpr);
	}

	@Override
	public Expression getConvertTypeExpression(Expression expr, String dbType, boolean nullable) {
		
		if(nullable) {
			switch(dbType) {
			case "12":
			case "35":
			case "8":
			case "16":
			case "7":
			case "10":
			case "27":
				return new InlineIfExpression(expr.isNull().binOp("||", expr._equals(new PhpStringLiteral(""))), Expressions.Null, getConvertTypeExpression(expr, dbType, false));
			default:
				return new InlineIfExpression(expr.isNull(), Expressions.Null, PhpFunctions.trim.call(expr));
			}
			
			
		} else {
			switch(dbType) {
			case "8":
			case "16":
			case "7":
				return expr.cast(Types.Int);
			case "14":	
			case "37":
				return new InlineIfExpression(expr.isNull(), new PhpStringLiteral(""), PhpFunctions.trim.call(expr));
			case "12":
				if(!expr.getType().equals(Types.DateTime)) {
					return new NewOperator(Types.DateTime,PhpFunctions.trim.call(expr)) ;
				} else {
					return expr;
				}
				
			case "10":
			case "27":
				return expr.cast(Types.Float);
			case "261":
				return expr;
			case "35":
				if(!expr.getType().equals(Types.DateTime)) {
					return new NewOperator(Types.DateTime,PhpFunctions.trim.call(expr)) ;
				} else {
					return expr;
				}
			case "13":
				return expr;
			default:
				return expr;
			}
		}
	}

	@Override
	public Expression getInsertUpdateValueExpression(Expression obj, Column col) {
		return getSaveConvertExpression(obj, col);
	}

	@Override
	public Expression getNullInsertUpdateValueExpression(Column col) {
		return Expressions.Null;
	}

	@Override
	public Expression getInsertUpdateValueGetterExpression(Expression obj, Column col) {
		return obj.callAttrGetter(new Attr(EntityCls.getTypeMapper().columnToType(col), col.getCamelCaseName()));
	}

	@Override
	public Type getDatabaseResultType() {
		// TODO Auto-generated method stub
		return Types.Mixed;
	}

	@Override
	public Expression getSaveConvertExpression(Expression obj, Column col) {
		String dbType = col.getDbType();
		switch(dbType) {
		case "12":
			return obj.callMethod(ClsDateTime.format, new PhpStringLiteral("Y-m-d"));
		case "35":
			return obj.callMethod(ClsDateTime.format, new PhpStringLiteral("Y-m-d H:i:s"));
		default:
			return obj;
		}
	}

	@Override
	public Expression getConvertFieldToStringExpression(Expression obj, Column col,Expression dateTimeFormatExpr,Expression dateFormatExpr) {
		Expression e = null;
		String dbType = col.getDbType();
		switch(dbType) {
		case "12":
			e = obj.callMethod(ClsDateTime.format, dateFormatExpr);
			break;
		case "35":
			e = obj.callMethod(ClsDateTime.format, dateTimeFormatExpr);
			break;
		case "37":
			e = obj;
			break;
		default:
			e = obj.cast(Types.String);
			break;
		}
		if(col.isNullable()) {
			return new InlineIfExpression(obj.isNull(), new PhpStringLiteral(""), e);
		}
		return e;
	}
	
	public Method getBeanRepositoryBeginTransactionMethod() {
		return new FirebirdEntityRepositoryBeginTransactionMethod();
	}

	@Override
	public Method getBeanRepositoryCommitTransactionMethod() {
		return new FirebirdEntityRepositoryCommitTransactionMethod();
	}

	@Override
	public Method getBeanRepositoryRollbackTransactionMethod() {
		return new FirebirdEntityRepositoryRollbackTransactionMethod();
	}
	
	@Override
	public boolean hasTransactionHandle() {
		return true;
	}

	@Override
	public Expression getConvertJsonValueToTypedExpression(Expression obj, Column col) {
		// TODO Auto-generated method stub
		return null;
	}

}
