package cpp.jsonentityquery.method;

import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.QString;
import cpp.lib.ClsBaseJsonEntitySelectQuery;
import database.column.Column;

public class MethodJsonQueryWhereIsNull extends Method {

	Column col;
	
	public MethodJsonQueryWhereIsNull(Cls query,Column col ) {
		super(Public, query.toRef(), getMethodName(col));
		this.col = col;
	}

	public static String getMethodName(Column col) {
		return "where"+col.getUc1stCamelCaseName() +"IsNull";
	}

	@Override
	public void addImplementation() {
		_callMethodInstr(_this(), ClsBaseJsonEntitySelectQuery.where,QString.fromStringConstant("e1."+col.getEscapedName()+"is null")  );
		_return(_this().dereference());
	}

}
