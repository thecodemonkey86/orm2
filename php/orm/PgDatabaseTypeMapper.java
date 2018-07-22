package php.orm;

import database.column.Column;
import php.bean.BeanCls;
import php.core.Attr;
import php.core.Type;
import php.core.Types;
import php.core.expression.Expression;
import php.core.method.Method;
import php.lib.ClsSqlParam;

public class PgDatabaseTypeMapper extends DatabaseTypeMapper{

	@Override
	public Type getTypeFromDbDataType(String dbType,boolean nullable) {
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
		return getTypeFromDbDataType(col.getDbType(),col.isNullable());
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

	@Override
	public Type getDatabaseLinkType() {
		// TODO Auto-generated method stub
		throw new RuntimeException("not impl");
	}

	@Override
	public Expression getDefaultFetchExpression(Expression res) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression getConvertTypeExpression(Expression e,String dbType, boolean nullable) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Expression getInsertUpdateValueExpression(Expression obj, Column col) {
		return Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(col)), obj);
			
	}

	@Override
	public Expression getNullInsertUpdateValueExpression(Column col) {
		return Types.SqlParam.callStaticMethod(ClsSqlParam.getNullMethodName(BeanCls.getTypeMapper().columnToType(col)));
	}

	@Override
	public Expression getInsertUpdateValueGetterExpression(Expression obj, Column col) {
		return Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(col)), obj.callAttrGetter(new Attr(BeanCls.getTypeMapper().columnToType(col), col.getCamelCaseName())));
	}

	@Override
	public Type getDatabaseResultType() {
		// TODO Auto-generated method stub
		return Types.Mixed;
	}

	@Override
	protected Expression getSaveConvertExpression(Expression obj, Column col) {
		// TODO Auto-generated method stub
		return obj;
	}

	@Override
	public Expression getConvertFieldToStringExpression(Expression obj, Column col,Expression dateTimeFormatExpr,Expression dateFormatExpr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Method getBeanRepositoryBeginTransactionMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Method getBeanRepositoryCommitTransactionMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Method getBeanRepositoryRollbackTransactionMethod() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
