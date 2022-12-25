package cpp.entity.method;

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
import cpp.entity.Nullable;
import database.column.Column;
import database.relation.PrimaryKey;

public class MethodSetPrimaryKey extends Method{
	PrimaryKey pk;
	public MethodSetPrimaryKey(PrimaryKey pk) {
		super(Public, Types.Void, "setPrimaryKey");
		this.pk = pk;
	
	}
	
	
	@Override
	public void addImplementation() {
		
		for(Column col:pk) {
			Attr a=parent.getAttrByName(col.getCamelCaseName());
			if (col.isNullable()) {
				TplCls nullable=(TplCls) a.getType();
				
				addParam(new Param(nullable.getElementType().isPrimitiveType() ? nullable.getElementType() : nullable.getElementType().toConstRef(), a.getName()));
			} else {
				addParam(new Param(a.getType().isPrimitiveType() ? a.getType() : a.getType().toConstRef(), a.getName()));
			}
		}
		
		Expression cond = null;
		for(Column col:pk) {
			String a=col.getCamelCaseName();
			Param param = getParam(a);
			if(col.isNullable()) {
				cond = _this().accessAttr(a).callMethod(Nullable.isNull).binOp(Operators.OR, param._notEquals(_this().accessAttr(a).callMethod(Nullable.val)));
			} else {
				cond = param._notEquals(_this().accessAttr(a));
			}
		}
		 IfBlock ifNotEquals = _if(cond);
		 IfBlock ifNotInsert=ifNotEquals.thenBlock()._ifNot(_this().accessAttr("insert"));
		for(Column col:pk) {
			String a=col.getCamelCaseName();
			Param param = getParam(a);
			 
			ifNotInsert.thenBlock().addInstr( _this().assignAttr(col.getCamelCaseName()+"Previous",  _this().accessAttr(a)));
			if (col.isNullable()) {
				ifNotEquals.thenBlock()._assign(_this().accessAttr(a), new CreateObjectExpression(Types.nullable(param.getType().isPrimitiveType() ? param.getType() : ((ConstRef)param.getType()).getBase()), param));
			} else {
				ifNotEquals.thenBlock()._assign(_this().accessAttr(a), param);
			}
					
					
		}
		ifNotInsert.thenBlock().
		addInstr(_this().assignAttr("primaryKeyModified",BoolExpression.TRUE));
	}
}
