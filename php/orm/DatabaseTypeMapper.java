package php.orm;

import php.cls.Type;
import php.cls.bean.BeanCls;
import php.cls.expression.Expression;
import php.cls.expression.Var;
import model.Column;

public abstract class DatabaseTypeMapper {
	public abstract Type getTypeFromDbDataType(String dbType);
	
	public abstract Expression getColumnDefaultValueExpression(Column col);
//	public abstract Expression getGenericDefaultValueExpression(Column col);
	
	public abstract Type columnToType(Column c);

	public abstract Type getSqlQueryClass();
	public abstract Type getBeanQueryClass(BeanCls beanCls);
	public abstract Type getLibBeanQueryClass(BeanCls beanCls);
}
