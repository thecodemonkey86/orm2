package cpp.bean.method;

import util.StringUtil;
import cpp.CoreTypes;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.expression.BoolExpression;
import database.column.Column;

public class MethodColumnAttrSetNull extends Method{

	Attr a;
	Column col;
	boolean internal;
	
	public MethodColumnAttrSetNull(Cls cls, Column col, Attr a, boolean internal) {
		super(Public, CoreTypes.Void, getMethodName(a)+(internal?"Internal":""));
		this.a=a;
		this.col=col;
		this.internal = internal;
	}

	@Override
	public void addImplementation() {
		addInstr(_accessThis(a).callMethodInstruction("setNull"));
		if(!internal) {
		if (!col.isPartOfPk())
			addInstr(_this().assignAttr(a.getName()+"Modified",BoolExpression.TRUE));
		}
		//_return(_this());
		
	}

	public static String getMethodName(Column col) {
		return "set"+col.getUc1stCamelCaseName()+"Null";
	}
	
	public static String getMethodName(Attr a) {
		return "set"+StringUtil.ucfirst( (!Column.isReserved(a.getName()) ? a.getName(): a.getName()+"_")+"Null");
	}

}
