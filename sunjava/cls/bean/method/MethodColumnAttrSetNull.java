package sunjava.cls.bean.method;

import model.Column;
import sunjava.cls.Attr;
import sunjava.cls.Method;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.expression.BoolExpression;
import sunjava.cls.expression.Expressions;
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
