package php.bean.method;

import database.column.Column;
import php.bean.EntityCls;
import php.core.Attr;
import php.core.Param;
import php.core.Types;
import php.core.expression.Expressions;
import php.core.method.Method;
import util.StringUtil;

public class MethodColumnAttrSetterInternal extends Method{

	Attr a;
	Column col;
	
	public MethodColumnAttrSetterInternal(EntityCls cls, Column col, Attr a) {
		super(Public, Types.Void, "set"+StringUtil.ucfirst(a.getName())+"Internal");
		this.a=a;
		addParam(new Param( a.getType() , a.getName(), col.isNullable() ? Expressions.Null : null));
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
		//_return(_this());
		
	}

	

}
