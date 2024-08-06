package php.orm;

import database.column.Column;
import php.core.Type;
import php.core.Types;
import php.core.expression.Expression;
import php.core.expression.PhpStringLiteral;
import php.core.method.Method;
import php.entity.EntityCls;
import php.lib.ClsSqlParam;

public abstract class DatabaseTypeMapper {
	public abstract Type getTypeFromDbDataType(String dbType,boolean nullable);
	public Type getTypeFromDbDataType(Column col) {
		if(col.getDbType()==null) {
			System.out.println(col.getName());
		}
		return getTypeFromDbDataType(col.getDbType(), col.isNullable());
	}
	public abstract Expression getColumnDefaultValueExpression(Column col);
	
	public abstract Type columnToType(Column c);

	public abstract Type getSqlQueryClass();
	public abstract Type getBeanQueryClass(EntityCls beanCls);
	public abstract Type getLibBeanQueryClass(EntityCls beanCls);

	public abstract Type getDatabaseLinkType() ;
	public abstract Type getDatabaseResultType() ;
	public abstract Expression getDefaultFetchExpression(Expression res);

	public abstract Expression getConvertTypeExpression(Expression e,String dbType,boolean nullable);
	public Expression getConvertTypeExpression(Expression e,Column col) {
		return getConvertTypeExpression(e,col.getDbType(),col.isNullable());
	}
	
	public Expression getConvertSqlParamExpression(Expression e,String dbType,boolean nullable) {
		Expression expr = getConvertTypeExpression(e, dbType, nullable);
		if(expr.getType() == null) {
			throw new NullPointerException();
		}
		if(expr.getType().equals(Types.DateTime))  {
			return Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(Types.DateTime), expr);
		}
		return expr;
	}
	public Expression getConvertSqlParamExpression(Expression e,Column col) {
		return getConvertSqlParamExpression(e,col.getDbType(),col.isNullable());
	}
	
	public String filterFetchAssocArrayKey(String key) {
		return key;
	}
	
	public Expression filterFetchAssocArrayKeyExpression(Expression keyExpr) {
		return keyExpr;
	}
	
	public abstract Expression getInsertUpdateValueExpression(Expression getter,Column col);
	public abstract Expression getInsertUpdateValueGetterExpression(Expression obj,Column col);
	public abstract Expression getNullInsertUpdateValueExpression(Column col);
	protected abstract Expression getSaveConvertExpression(Expression obj,Column col);
	public Expression getConvertFieldToStringExpression(Expression obj, Column col)  {
		return getConvertFieldToStringExpression(obj, col, new PhpStringLiteral("Y-m-d H:i:s") ,new PhpStringLiteral("Y-m-d"));
	}
	public abstract Expression getConvertFieldToStringExpression(Expression obj, Column col,Expression dateTimeFormatExpr,Expression dateFormatExpr) ;
	
	public abstract Expression getConvertJsonValueToTypedExpression(Expression obj, Column col) ;
	public abstract Method getBeanRepositoryBeginTransactionMethod() ;
	public abstract Method getBeanRepositoryCommitTransactionMethod() ;
	public abstract Method getBeanRepositoryRollbackTransactionMethod() ;
	public boolean hasTransactionHandle() {
		return false;
	}
	
}
