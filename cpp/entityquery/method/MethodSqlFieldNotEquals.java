package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import database.column.Column;

public class MethodSqlFieldNotEquals extends Method{
	Column col;
	Param pAlias;
	
	public MethodSqlFieldNotEquals(Column col, boolean withAlias) {
		super(Public, Types.QString, "field"+ col.getUc1stCamelCaseName()+"NotEquals");
		this.col = col;
		setStatic(true);
		if(withAlias) {
			pAlias = addParam(new Param(Types.QString.toConstRef(), "alias"));
		} else {
			pAlias = null;
		}
	}

	@Override
	public void addImplementation() {
		if(pAlias != null) {
			_return(QString.concat(pAlias, QString.fromStringConstant("."+col.getEscapedName()+"<>?")));
		} else {
			_return(QString.fromStringConstant(col.getEscapedName()+"<>?"));
		}
		
		
	}

}
