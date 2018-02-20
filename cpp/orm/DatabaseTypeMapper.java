package cpp.orm;

import cpp.core.Method;
import cpp.core.Type;
import cpp.core.expression.Expression;
import database.column.Column;

public abstract class DatabaseTypeMapper {
	public abstract Method getQVariantConvertMethod(String dbType);
	public abstract Type getTypeFromDbDataType(String dbType, boolean nullable);
	
	public abstract Expression getColumnDefaultValueExpression(Column col);
	public abstract Expression getGenericDefaultValueExpression(Column col);
	
	public abstract Type columnToType(Column c);
}
