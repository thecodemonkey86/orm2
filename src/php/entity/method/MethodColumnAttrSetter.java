package php.entity.method;

import database.column.Column;
import php.core.Attr;
import php.core.Param;
import php.core.Types;
import php.core.expression.BoolExpression;
import php.core.expression.Expressions;
import php.core.instruction.IfBlock;
import php.core.method.Method;
import php.entity.EntityCls;
import util.StringUtil;

public class MethodColumnAttrSetter extends Method {

	Attr a;
	Column col;
	EntityCls entity;
	
	public MethodColumnAttrSetter(EntityCls cls, Column col, Attr a) {
		super(Public, Types.Void, getMethodName(col));
		this.a = a;
		addParam(new Param(a.getType(), a.getName(), col.isNullable() ? Expressions.Null : null));
		this.col = col;
		this.entity = cls;
	}

	public static String getMethodName(Column col) {
		return "set" + StringUtil.ucfirst(col.getCamelCaseName());
	}

	@Override
	public void addImplementation() {
		//addThrowsException(Types.SqlException);
		Param param = getParam(a.getName());
		IfBlock ifNotEq=_if(_this().accessAttr(a.getName())._notEquals(param));
		ifNotEq.thenBlock(). addInstr(_this().assignAttr(a.getName() + "Modified",
				BoolExpression.TRUE));
		ifNotEq.thenBlock(). _assign(_accessThis(a), param);
		//_return(_this());

	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

}
