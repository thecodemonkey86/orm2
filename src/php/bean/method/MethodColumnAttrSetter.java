package php.bean.method;

import database.column.Column;
import php.bean.EntityCls;
import php.core.Attr;
import php.core.Param;
import php.core.Types;
import php.core.expression.BoolExpression;
import php.core.expression.Expression;
import php.core.expression.Expressions;
import php.core.instruction.AssignInstruction;
import php.core.instruction.IfBlock;
import php.core.method.Method;
import php.lib.ClsBaseEntity;
import util.StringUtil;

public class MethodColumnAttrSetter extends Method {

	Attr a;
	Column col;
	EntityCls bean;
	
	public MethodColumnAttrSetter(EntityCls cls, Column col, Attr a) {
		super(Public, Types.Void, "set" + StringUtil.ucfirst(a.getName()));
		this.a = a;
		addParam(new Param(a.getType(), a.getName(), col.isNullable() ? Expressions.Null : null));
		this.col = col;
		this.bean = cls;
	}

	@Override
	public void addImplementation() {
		//addThrowsException(Types.SqlException);
		Param param = getParam(a.getName());
		
		if (!col.isPartOfPk()) {
			addInstr(_this().assignAttr(a.getName() + "Modified",
					BoolExpression.TRUE));
			_assign(_accessThis(a), param);
		} else {
			IfBlock ifNotInsert = _if(_not(_this().accessAttr(ClsBaseEntity.insert)));
			for(Column colPk : bean.getTbl().getPrimaryKey().getColumns()) {
				Expression prev = EntityCls.accessColumnAttrOrEntityPrevious(_this(), colPk);
				if(colPk.hasOneRelation()) {
					IfBlock ifNotNull = ifNotInsert.thenBlock()._if(_not(EntityCls.accessColumnAttrOrEntity(_this(), colPk).isNull()));
					ifNotNull.thenBlock().addInstr(new AssignInstruction(prev, EntityCls.accessAttrGetterByColumn(_this(), colPk,false)));
				} else {
					ifNotInsert.thenBlock().addInstr(new AssignInstruction(prev, EntityCls.accessAttrGetterByColumn(_this(), colPk,false)));
				}
			}
			_assign(_accessThis(a), param);
			ifNotInsert.thenBlock().addInstr(_this().assignAttr("primaryKeyModified",
					BoolExpression.TRUE));
		}
		//_return(_this());

	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

}
