package cpp.bean.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.lib.ClsQString;
import database.column.Column;

public class MethodGetFieldNameAlias extends Method {

	protected Column col;
	Param pAlias;
	
	public MethodGetFieldNameAlias(Column col) {
		super(Public, Types.QString, getMethodName(col));
		setStatic(true);
		this.col = col;
		pAlias = addParam(new Param(Types.QString.toConstRef(), "alias"));	
	}

	@Override
	public void addImplementation() {
		_return(QString.fromStringConstant("%1.%2").callMethod(ClsQString.arg, pAlias, QString.fromStringConstant(col.getName())));	
	}
	
	public static String getMethodName(Column col) {
		return "fieldAlias" + col.getUc1stCamelCaseName();
	}

}
