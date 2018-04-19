package cpp.beanquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.QString;
import database.column.Column;

public class MethodSqlFieldEquals extends Method{
	Column col;
	public MethodSqlFieldEquals(Column col) {
		super(Public, Types.QString, "field"+ col.getUc1stCamelCaseName()+"Equals");
		this.col = col;
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		_return(QString.fromStringConstant(col.getEscapedName()+"=?"));
		
	}

}
