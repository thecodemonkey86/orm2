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
}
