package cpp.orm;

import cpp.cls.Method;
import cpp.cls.Type;
import cpp.cls.expression.Expression;
import model.Column;

public abstract class DatabaseTypeMapper {
	public abstract Method getQVariantConvertMethod(String dbType);
	public abstract Type getTypeFromDbDataType(String dbType, boolean nullable);
	
	public abstract Expression getColumnDefaultValueExpression(Column col);
	public abstract Expression getGenericDefaultValueExpression(Column col);
	
	public abstract Type columnToType(Column c);
}
