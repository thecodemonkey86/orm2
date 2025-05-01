package cpp.entity.method;

import util.CodeUtil2;
import util.StringUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.Type;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.Operators;
import cpp.core.instruction.IfBlock;
import cpp.entity.EntityCls;
import cpp.core.Optional;
import cpp.entity.SetterValidator;
import cpp.entity.SetterValidator.OnFailValidateMode;
import database.column.Column;

public class MethodNullableColumnAttrSetter extends Method{

	Attr a;
	Column col;
	
	public MethodNullableColumnAttrSetter( Column col, Attr a) {
		super(Public, Types.Void, getMethodName(col));
		this.a=a;
		addParam(new Param(a.getType().isPrimitiveType() ? a.getType() : a.getType().toConstRef(), a.getName()));
		this.col=col;
	}
	
	@Override
	public void addImplementation() {
		Param param = getParam(a.getName());
		Expression cond = null;
		EntityCls entityCls = (EntityCls) parent;
		boolean returnBool = false;
		if(entityCls.hasColumnValidator(col.getName())) {
			SetterValidator columnValidator = entityCls.getColumnValidator(col.getName());
			if(columnValidator.onFailMode() ==OnFailValidateMode.ReturnFalse) {
				returnBool = true;
				setReturnType(Types.Bool);
			}
			_ifNot(new Expression() {
				
				@Override
				public String toString() {
					return CodeUtil2.parentheses(columnValidator.getCondition());
				}
				
				@Override
				public Type getType() {
					return Types.Bool;
				}
			}).thenBlock()._return(columnValidator.onFailMode() ==OnFailValidateMode.ReturnFalse ? BoolExpression.FALSE: null);
		}
		
		cond = _not(_this().accessAttr(a).callMethod(Optional.has_value)).binOp(Operators.OR, param._notEquals(_this().accessAttr(a)));
		
	
		 IfBlock ifNotEquals = _if(cond);
		 ifNotEquals.thenBlock(). addInstr(_this().assignAttr(a.getName()+"Modified",BoolExpression.TRUE));
		ifNotEquals.thenBlock()._assign(_accessThis(a),  param);
		if(returnBool) {
		 _return(BoolExpression.TRUE);
		}
		
	}

	public static String getMethodName(Column col) {
		return "set"+StringUtil.ucfirst(col.getCamelCaseName());
	}

}
