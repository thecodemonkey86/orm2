package cpp.entity.method;

import util.CodeUtil2;
import util.StringUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Optional;
import cpp.core.Param;
import cpp.core.TplCls;
import cpp.core.Type;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.Operators;
import cpp.core.instruction.IfBlock;
import cpp.entity.EntityCls;
import cpp.entity.SetterValidator;
import cpp.entity.SetterValidator.OnFailValidateMode;
import database.column.Column;

public class MethodColumnAttrSetter extends Method{

	Attr a;
	Column col;
	
	public MethodColumnAttrSetter( Column col, Attr a) {
		super(Public, Types.Void, getMethodName(col));
		this.a=a;
		if (col.isNullable()) {
			TplCls nullable=(TplCls) a.getType();
			
			addParam(new Param(nullable.getElementType().isPrimitiveType() ? nullable.getElementType() : nullable.getElementType().toConstRef(), a.getName()));
		} else {
			addParam(new Param(a.getType().isPrimitiveType() ? a.getType() : a.getType().toConstRef(), a.getName()));
		}
		this.col=col;
		setnoexcept();
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
		
		if(col.isNullable()) {
			cond =_not(_this().accessAttr(a).callMethod(Optional.has_value)).binOp(Operators.OR, param._notEquals(_this().accessAttr(a).callMethod(Optional.value)));
		} else {
			cond = param._notEquals(_this().accessAttr(a));
		}
		
	
		 IfBlock ifNotEquals = _if(cond);
		if (!col.isPartOfPk())
			ifNotEquals.thenBlock(). addInstr(_this().assignAttr(a.getName()+"Modified",BoolExpression.TRUE));
		else {
			IfBlock ifNotInsert=ifNotEquals.thenBlock()._ifNot(_this().accessAttr("insert"));
			ifNotInsert.thenBlock().addInstr( _this().assignAttr(col.getCamelCaseName()+"Previous",  _this().accessAttr(a)));
			ifNotInsert.thenBlock().
				addInstr(_this().assignAttr("primaryKeyModified",BoolExpression.TRUE));
		}
		if (col.isNullable()) {
//			ifNotEquals.thenBlock()._assign(_accessThis(a), new CreateObjectExpression(Types.optional(param.getType().isPrimitiveType() ? param.getType() : ((ConstRef)param.getType()).getBase()), param));
			ifNotEquals.thenBlock()._callMethodInstr(_accessThis(a), Optional.emplace, param);
		} else {
			ifNotEquals.thenBlock()._assign(_accessThis(a), param);
		}
		if(returnBool) {
		 _return(BoolExpression.TRUE);
		}
		
	}

	public static String getMethodName(Column col) {
		return "set"+StringUtil.ucfirst(col.getCamelCaseName());
	}

}
