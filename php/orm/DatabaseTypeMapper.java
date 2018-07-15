package php.orm;

import database.column.Column;
import php.bean.BeanCls;
import php.core.Type;
import php.core.expression.Expression;

public abstract class DatabaseTypeMapper {
	public abstract Type getTypeFromDbDataType(String dbType);
	
	public abstract Expression getColumnDefaultValueExpression(Column col);
	
	public abstract Type columnToType(Column c);

	public abstract Type getSqlQueryClass();
	public abstract Type getBeanQueryClass(BeanCls beanCls);
	public abstract Type getLibBeanQueryClass(BeanCls beanCls);

	public abstract Type getDatabaseLinkType() ;
	public abstract Expression getDefaultFetchExpression(Expression res);

	public abstract Expression getConvertTypeExpression(Expression e,String dbType,boolean nullable);
	public Expression getConvertTypeExpression(Expression e,Column col) {
		return getConvertTypeExpression(e,col.getDbType(),col.isNullable());
	}
	
	public String filterFetchAssocArrayKey(String key) {
		return key;
	}
	
	public Expression filterFetchAssocArrayKeyExpression(Expression keyExpr) {
		return keyExpr;
	}
	
}
