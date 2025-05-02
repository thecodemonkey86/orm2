package cpp.jsonentity.method;

import util.StringUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.ConstRef;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.TplCls;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.Operators;
import cpp.core.instruction.IfBlock;
import cpp.core.Optional;
import database.column.Column;

public class MethodColumnAttrSetter extends Method{

	Attr a;
	Column col;
	
	public MethodColumnAttrSetter( Column col, Attr a) {
		super(Public, Types.Void, getMethodName(col));
		this.a=a;
		assert !col.isPartOfPk();
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
		Expression cond = null;
		
		if(col.isNullable()) {
			cond = _not(_this().accessAttr(a).callMethod(Optional.has_value)).binOp(Operators.OR, param._notEquals(_this().accessAttr(a).callMethod(Optional.value)));
		} else {
			cond = param._notEquals(_this().accessAttr(a));
		}
		
	
		 IfBlock ifNotEquals = _if(cond);
		 ifNotEquals.thenBlock(). addInstr(_this().assignAttr(a.getName()+"Modified",BoolExpression.TRUE));
		if (col.isNullable()) {
			ifNotEquals.thenBlock()._assign(_accessThis(a), new CreateObjectExpression(Types.optional(param.getType().isPrimitiveType() ? param.getType() : ((ConstRef)param.getType()).getBase()), param));
		} else {
			ifNotEquals.thenBlock()._assign(_accessThis(a), param);
		}
		//_return(_this());
		
	}

	public static String getMethodName(Column col) {
		return "set"+StringUtil.ucfirst(col.getCamelCaseName());
	}

}
