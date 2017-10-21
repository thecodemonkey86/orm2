package cpp.bean.method;

import util.StringUtil;
import cpp.Types;
import cpp.bean.BeanCls;
import cpp.core.Attr;
import cpp.core.ConstRef;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.TplCls;
import cpp.core.expression.CreateObjectExpression;
import database.column.Column;

public class MethodColumnAttrSetterInternal extends Method{

	Attr a;
	Column col;
	
	public MethodColumnAttrSetterInternal(BeanCls cls, Column col, Attr a) {
		super(Public, cls.toRawPointer(), "set"+StringUtil.ucfirst(a.getName())+"Internal");
		this.a=a;
		if (col.isNullable()) {
			TplCls nullable=(TplCls) a.getType();
			
			addParam(new Param(nullable.getElementType().isPrimitiveType() ? nullable.getElementType() : nullable.getElementType().toConstRef(), a.getName()));
		} else {
			addParam(new Param(a.getType().isPrimitiveType() ? a.getType() : a.getType().toConstRef(), a.getName()));
		}
		this.col=col;
	}

	@Override
	public void addImplementation() {
		Param param = getParam(a.getName());
		if (col.isNullable()) {
			_assign(_accessThis(a), new CreateObjectExpression(Types.nullable(param.getType().isPrimitiveType() ? param.getType() : ((ConstRef)param.getType()).getBase()), param));
		} else {
			_assign(_accessThis(a), param);
		}
		_return(_this());
		
	}

	

}
