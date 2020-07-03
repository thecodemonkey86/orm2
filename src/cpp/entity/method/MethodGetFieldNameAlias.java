package cpp.entity.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.PlusOperator;
import database.column.Column;

public class MethodGetFieldNameAlias extends Method {

	protected Column col;
	Param pAlias;
	boolean mainAlias;
	
	public MethodGetFieldNameAlias(Column col, boolean mainAlias) {
		super(Public, Types.QString, getMethodName(col));
		setStatic(true);
		this.col = col;
		if(!mainAlias)
			pAlias = addParam(new Param(Types.QString.toConstRef(), "alias"));
		
		this.mainAlias = mainAlias;
	}

	@Override
	public void addImplementation() {
		if(mainAlias) {
			_return(QString.fromLatin1StringConstant("e1." + col.getEscapedName()));
		} else {
			_return(  pAlias.binOp(PlusOperator.SYMBOL, QString.fromStringConstant("." + col.getEscapedName())));	
		}
			
	}
	
	public static String getMethodName(Column col) {
		return "fieldAlias" + col.getUc1stCamelCaseName();
	}

}
