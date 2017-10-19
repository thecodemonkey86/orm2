package cpp.cls.bean.method;

import model.Column;
import util.StringUtil;
import cpp.cls.Attr;
import cpp.cls.Method;
import cpp.cls.bean.BeanCls;
import cpp.cls.expression.BoolExpression;

public class MethodColumnAttrSetNull extends Method{

	Attr a;
	Column col;
	boolean internal;
	
	public MethodColumnAttrSetNull(BeanCls cls, Column col, Attr a, boolean internal) {
		super(Public, cls.toRawPointer(), "set"+StringUtil.ucfirst(a.getName()+"Null"+(internal?"Internal":"")));
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
		_return(_this());
		
	}

	

}
