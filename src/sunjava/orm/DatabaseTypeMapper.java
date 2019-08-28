package sunjava.orm;

import database.column.Column;
import sunjava.bean.BeanCls;
import sunjava.core.Type;
import sunjava.core.expression.Expression;
import sunjava.core.expression.Var;

public abstract class DatabaseTypeMapper {
	public abstract Type getTypeFromDbDataType(String dbType, boolean nullable);
	
	public abstract Expression getDefaultValueExpression(Column col,String str);
	
	public abstract Type columnToType(Column c);

	public abstract Expression getResultSetValueGetter(Var resultSet,Column col,Expression alias);
	public abstract Expression getResultSetValueGetter(Var resultSet,Column col,String alias);

	protected abstract Expression getResultSetValueGetterInternal(Var resultSet,Column col,Expression identifier);
	
	public abstract Type getSqlQueryClass();
	public abstract Type getBeanQueryClass(BeanCls beanCls);
	public abstract Type getLibBeanQueryClass(BeanCls beanCls);
}
