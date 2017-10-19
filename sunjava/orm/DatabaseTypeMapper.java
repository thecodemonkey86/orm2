package sunjava.orm;

import sunjava.cls.Type;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.expression.Expression;
import sunjava.cls.expression.Var;
import model.Column;

public abstract class DatabaseTypeMapper {
	public abstract Type getTypeFromDbDataType(String dbType, boolean nullable);
	
	public abstract Expression getDefaultValueExpression(Column col,String str);
//	public abstract Expression getGenericDefaultValueExpression(Column col);
	
	public abstract Type columnToType(Column c);

	public abstract Expression getResultSetValueGetter(Var resultSet,Column col,Expression alias);
	public abstract Expression getResultSetValueGetter(Var resultSet,Column col,String alias);

	protected abstract Expression getResultSetValueGetterInternal(Var resultSet,Column col,Expression identifier);
	
	public abstract Type getSqlQueryClass();
	public abstract Type getBeanQueryClass(BeanCls beanCls);
	public abstract Type getLibBeanQueryClass(BeanCls beanCls);
}
