package php.cls.bean.method;

import model.Column;
import php.cls.Attr;
import php.cls.Method;
import php.cls.Param;
import php.cls.bean.BeanCls;
import php.cls.expression.Expressions;
import util.StringUtil;

public class MethodColumnAttrSetterInternal extends Method{

	Attr a;
	Column col;
	
	public MethodColumnAttrSetterInternal(BeanCls cls, Column col, Attr a) {
		super(Public, cls, "set"+StringUtil.ucfirst(a.getName())+"Internal");
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
		_return(_this());
		
	}

	

}
