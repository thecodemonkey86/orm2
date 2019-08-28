package cpp.orm;

import cpp.core.Method;
import cpp.core.Type;
import cpp.core.expression.Expression;
import cpp.lib.ClsSqlQuery;
import database.column.Column;

public abstract class DatabaseTypeMapper {
	public Method getQVariantConvertMethod(Column col) {
		return getQVariantConvertMethod(col.getDbType());
	}
	public abstract Method getQVariantConvertMethod(String pgType);
	public abstract Type getTypeFromDbDataType(String dbType, boolean nullable);
	
	public abstract Expression getColumnDefaultValueExpression(Column col);
	public abstract Expression getGenericDefaultValueExpression(Column col);
	
	public abstract ClsSqlQuery getSqlQueryType();
	public abstract Type columnToType(Column c);
}
