package cpp.orm;

import cpp.core.Method;
import cpp.core.MethodTemplate;
import cpp.core.Type;
import cpp.core.expression.Expression;
import cpp.entity.EntityCls;
import cpp.entityquery.method.MethodToStringSelect;
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
	public final Type columnToType(Column c) {
		return columnToType(c,c.isNullable());
	}
	public abstract Type columnToType(Column c,boolean nullable);
	public abstract MethodTemplate getInsertOrIgnoreMethod(boolean byref);
//	public abstract String getRepositoryInsertOrIgnoreMethod();
//	public abstract String getRepositoryPrepareInsertOrIgnoreMethod();
	public Method getSelectToStringMethod(EntityCls cls) {
		return  new MethodToStringSelect(cls);
	}
}
