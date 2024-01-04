package php.entity.method;

import java.util.ArrayList;

import database.column.Column;
import database.relation.PrimaryKey;
import php.core.Attr;
import php.core.Param;
import php.core.Types;
import php.core.expression.BoolExpression;
import php.core.expression.Expression;
import php.core.expression.Expressions;
import php.core.expression.Operators;
import php.core.instruction.IfBlock;
import php.core.method.Method;
import php.entity.EntityCls;

public class MethodSetPrimaryKey extends Method{
	EntityCls cls;
	public MethodSetPrimaryKey(EntityCls cls) {
		super(Public, Types.Void, getMethodName(cls));
		this.cls = cls;
		
	}
	
	
	@Override
	public void addImplementation() {
		PrimaryKey pk=cls.getTbl().getPrimaryKey();

		for(Column col:pk) {
			Attr a=cls.getAttrByName(col.getCamelCaseName());
			if (col.isNullable()) {
				addParam(new Param(a.getType().toNullable(), a.getName()));
			} else {
				addParam(new Param(a.getType(), a.getName()));
			}
		}
		ArrayList<Expression> cond = new ArrayList<>();
		for(Column col:pk) {
			String a=col.getCamelCaseName();
			Param param = getParam(a);
			if(col.isNullable()) {
				cond.add( _this().accessAttr(a)._equals(Expressions.Null).binOp(Operators.OR, param._notEquals(_this().accessAttr(a))));
			} else {
				cond.add(param._notEquals(_this().accessAttr(a)));
			}
		}
		 IfBlock ifNotEquals = _if(Expressions.or(cond) );
		 IfBlock ifNotInsert=ifNotEquals.thenBlock()._if(Expressions.not(_this().accessAttr("insert")));
		for(Column col:pk) {
			String a=col.getCamelCaseName();
			Param param = getParam(a);
			 
			ifNotInsert.thenBlock().addInstr( _this().assignAttr(col.getCamelCaseName()+"Previous",  _this().accessAttr(a)));
			ifNotEquals.thenBlock()._assign(_this().accessAttr(a), param);
					
					
		}
		ifNotInsert.thenBlock().
		addInstr(_this().assignAttr("primaryKeyModified",BoolExpression.TRUE));
	}


	public static String getMethodName(EntityCls cls) {
		return cls.getTbl().getPrimaryKey().getColumnCount()>1? "setPrimaryKey" : "set"+cls.getTbl().getPrimaryKey().getColumn(0).getUc1stCamelCaseName();
	}
}
