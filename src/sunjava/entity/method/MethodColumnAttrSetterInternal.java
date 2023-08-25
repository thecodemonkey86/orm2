package sunjava.entity.method;

import database.column.Column;
import sunjava.core.Attr;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.PrimitiveType;
import sunjava.entity.EntityCls;
import util.StringUtil;

public class MethodColumnAttrSetterInternal extends Method{

	Attr a;
	Column col;
	
	public MethodColumnAttrSetterInternal(EntityCls cls, Column col, Attr a) {
		super(Public, cls, "set"+StringUtil.ucfirst(a.getName())+"Internal");
		this.a=a;
		if (col.isNullable() && a.getType() instanceof PrimitiveType) {
			addParam(new Param( ((PrimitiveType)a.getType()).getAutoBoxingClass() , a.getName()));
		} else {
			addParam(new Param( a.getType() , a.getName()));
		}
		this.col=col;
	}

	@Override
	public void addImplementation() {
		Param param = getParam(a.getName());
//		if (col.isNullable()) {
//			_assign(_accessThis(a), new NewOperator(param.getType(), param));
//		} else {
//			_assign(_accessThis(a), param);
//		}
		_assign(_accessThis(a), param);
		_return(_this());
		
	}

	

}
