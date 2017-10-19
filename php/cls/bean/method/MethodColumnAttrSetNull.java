package php.cls.bean.method;

import model.Column;
import php.cls.Attr;
import php.cls.Method;
import php.cls.bean.BeanCls;
import php.cls.expression.BoolExpression;
import php.cls.expression.Expressions;
import util.StringUtil;

public class MethodColumnAttrSetNull extends Method{

	Attr a;
	Column col;
	
	public MethodColumnAttrSetNull(BeanCls cls, Column col, Attr a) {
		super(Public, cls, "set"+StringUtil.ucfirst(a.getName()+"Null"));
		this.a=a;
		this.col=col;
	}

	@Override
	public void addImplementation() {
		addInstr(_this().assignAttr(a.getName(),Expressions.Null));
		if (!col.isPartOfPk())
			addInstr(_this().assignAttr(a.getName()+"Modified",BoolExpression.TRUE));
		_return(_this());
		
	}

	

}
