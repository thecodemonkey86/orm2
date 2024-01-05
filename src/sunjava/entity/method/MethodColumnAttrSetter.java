package sunjava.entity.method;

import database.column.Column;
import sunjava.core.Attr;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.PrimitiveType;
import sunjava.core.expression.BoolExpression;
import sunjava.core.expression.Expression;
import sunjava.core.instruction.AssignInstruction;
import sunjava.core.instruction.IfBlock;
import sunjava.entity.EntityCls;
import util.StringUtil;

public class MethodColumnAttrSetter extends Method {

	Attr a;
	Column col;
	EntityCls entity;
	
	public MethodColumnAttrSetter(EntityCls cls, Column col, Attr a) {
		super(Public, cls, "set" + StringUtil.ucfirst(a.getName()));
		this.a = a;
		if (col.isNullable() && a.getType() instanceof PrimitiveType) {
			addParam(new Param(
					((PrimitiveType) a.getType()).getAutoBoxingClass(),
					a.getName()));
		} else {
			addParam(new Param(a.getType(), a.getName()));
		}
		this.col = col;
		this.entity = cls;
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
			for(Column colPk : entity.getTbl().getPrimaryKey().getColumns()) {
				Expression prev = EntityCls.accessColumnAttrOrEntityPrevious(_this(), colPk);
				if(colPk.hasOneRelation()) {
					IfBlock ifNotNull = _if(_not(EntityCls.accessColumnAttrOrEntity(_this(), colPk).isNull()));
					ifNotNull.thenBlock().addInstr(new AssignInstruction(prev, EntityCls.accessAttrGetterByColumn(_this(), colPk,false)));
				} else {
					addInstr(new AssignInstruction(prev, EntityCls.accessAttrGetterByColumn(_this(), colPk,false)));
				}
			}
			_assign(_accessThis(a), param);
			addInstr(_this().assignAttr("primaryKeyModified",
					BoolExpression.TRUE));
		}
		_return(_this());

	}

}
