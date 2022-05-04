package php.orm;

import database.column.Column;
import php.core.Attr;
import php.core.Type;
import php.core.Types;
import php.core.expression.BoolExpression;
import php.core.expression.DoubleExpression;
import php.core.expression.Expression;
import php.core.expression.Expressions;
import php.core.expression.InlineIfExpression;
import php.core.expression.IntExpression;
import php.core.expression.NewOperator;
import php.core.expression.PhpStringLiteral;
import php.core.method.Method;
import php.entity.EntityCls;
import php.entityrepository.method.MysqliBeanRepositoryBeginTransactionMethod;
import php.entityrepository.method.MysqliBeanRepositoryCommitTransactionMethod;
import php.entityrepository.method.MysqliBeanRepositoryRollbackTransactionMethod;
import php.lib.ClsDateTime;
import php.lib.ClsMysqliResult;
import php.lib.ClsSqlParam;

public class MySqlDatabaseTypeMapper extends DatabaseTypeMapper{
	
	@Override
	public Type getTypeFromDbDataType(String dbType,boolean nullable) {
		if(nullable) {
			return getTypeFromDbDataType(dbType, false).toNullable();
		}
		switch(dbType) {
		case "int":
		case "tinyint":
		case "bigint":
		case "int_unsigned":
		case "bigint_unsigned":
			return Types.Int;
		case "smallint":
		case "smallint_unsigned":
			return Types.Int;
		case "varchar":
		case "character":	
		case "text":
		case "mediumtext":
			return Types.String;
		case "date":
		case "datetime":
		case "timestamp":
			return Types.DateTime;
		case "double precision":
		case "numeric":
			return Types.Float;
		case "bytea":
			return Types.String;	
		case "boolean":
			return Types.Bool;		
		default:
			System.out.println(dbType);
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
			case "timestamp":
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
		return getTypeFromDbDataType(col.getDbType(),col.isNullable());
	}


	@Override
	public Type getSqlQueryClass() {
		return Types.MysqlSqlQuery;
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
		return Types.mysqli;
	}

	@Override
	public Expression getDefaultFetchExpression(Expression res) {
		return res.callMethod(ClsMysqliResult.fetch_assoc);
	}

	@Override
	public Expression getConvertTypeExpression(Expression arg,String dbType, boolean nullable) {
		
		switch(dbType) {
		case "date":
		case "datetime":			
		case "timestamp":
			if(nullable) {
				return new InlineIfExpression(arg.isNull(), Expressions.Null, new NewOperator(Types.DateTime, arg));
			}
			return arg.getType().equals(Types.DateTime) ? arg : new NewOperator(Types.DateTime, arg);
		case "varchar":
		case "character":	
		case "text":
			return arg;
		case "int":
		case "bigint":
		case "smallint":
			return arg.cast(Types.Int);
		case "double precision":
		case "numeric":
			return arg.cast(Types.Float);
		default:
			return arg;
		}
		
	}

	@Override
	public Expression getInsertUpdateValueExpression(Expression obj, Column col) {
		return Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(EntityCls.getTypeMapper().columnToType(col)), getSaveConvertExpression(obj,col));
			
	}
	
	@Override
	public Expression getInsertUpdateValueGetterExpression(Expression obj, Column col) {
		return Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(EntityCls.getTypeMapper().columnToType(col)), getSaveConvertExpression(obj.callAttrGetter(new Attr(EntityCls.getTypeMapper().columnToType(col), col.getCamelCaseName())),col));
	}

	@Override
	public Expression getNullInsertUpdateValueExpression(Column col) {
		return Types.SqlParam.callStaticMethod(ClsSqlParam.getNullMethodName(EntityCls.getTypeMapper().columnToType(col)));
	}

	@Override
	public Type getDatabaseResultType() {
		// TODO Auto-generated method stub
		return Types.mysqli_result;
	}

	@Override
	protected Expression getSaveConvertExpression(Expression obj, Column col) {
		return obj;
	}

	@Override
	public Expression getConvertFieldToStringExpression(Expression obj, Column col,Expression dateTimeFormatExpr,Expression dateFormatExpr) {
		Expression e = null;
		
		String dbType = col.getDbType();
		switch(dbType) {
		case "date":
			e = obj.callMethod(ClsDateTime.format, dateFormatExpr);
			break;
		case "datetime":
		case "timestamp":
			e = obj.callMethod(ClsDateTime.format, dateTimeFormatExpr);
			break;
		case "varchar":
		case "character":	
		case "text":
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

	@Override
	public Method getBeanRepositoryBeginTransactionMethod() {
		return new MysqliBeanRepositoryBeginTransactionMethod();
	}

	@Override
	public Method getBeanRepositoryCommitTransactionMethod() {
		return new MysqliBeanRepositoryCommitTransactionMethod();
	}

	@Override
	public Method getBeanRepositoryRollbackTransactionMethod() {
		return new MysqliBeanRepositoryRollbackTransactionMethod();
	}

	


	
}
