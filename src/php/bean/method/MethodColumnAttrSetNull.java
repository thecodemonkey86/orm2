package php.bean.method;

import database.column.Column;
import php.bean.BeanCls;
import php.core.Attr;
import php.core.Types;
import php.core.expression.BoolExpression;
import php.core.expression.Expressions;
import php.core.method.Method;
import util.StringUtil;

public class MethodColumnAttrSetNull extends Method{

	Attr a;
	Column col;
	
	public MethodColumnAttrSetNull(BeanCls cls, Column col, Attr a) {
		super(Public, Types.Void, "set"+StringUtil.ucfirst(a.getName()+"Null"));
		this.a=a;
		this.col=col;
	}

	@Override
	public void addImplementation() {
		addInstr(_this().assignAttr(a.getName(),Expressions.Null));
		if (!col.isPartOfPk())
			addInstr(_this().assignAttr(a.getName()+"Modified",BoolExpression.TRUE));
		//_return(_this());
		
	}

	

}
