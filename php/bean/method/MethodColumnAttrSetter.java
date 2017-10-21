package php.bean.method;

import database.column.Column;
import php.bean.BeanCls;
import php.core.Attr;
import php.core.Param;
import php.core.expression.BoolExpression;
import php.core.expression.Expression;
import php.core.expression.Expressions;
import php.core.instruction.AssignInstruction;
import php.core.instruction.IfBlock;
import php.core.method.Method;
import util.StringUtil;

public class MethodColumnAttrSetter extends Method {

	Attr a;
	Column col;
	BeanCls bean;
	
	public MethodColumnAttrSetter(BeanCls cls, Column col, Attr a) {
		super(Public, cls, "set" + StringUtil.ucfirst(a.getName()));
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
			for(Column colPk : bean.getTbl().getPrimaryKey().getColumns()) {
				Expression prev = BeanCls.accessColumnAttrOrEntityPrevious(_this(), colPk);
				if(colPk.hasOneRelation()) {
					IfBlock ifNotNull = _if(_not(BeanCls.accessColumnAttrOrEntity(_this(), colPk).isNull()));
					ifNotNull.thenBlock().addInstr(new AssignInstruction(prev, BeanCls.accessAttrGetterByColumn(_this(), colPk,false)));
				} else {
					addInstr(new AssignInstruction(prev, BeanCls.accessAttrGetterByColumn(_this(), colPk,false)));
				}
			}
			_assign(_accessThis(a), param);
			addInstr(_this().assignAttr("primaryKeyModified",
					BoolExpression.TRUE));
		}
		_return(_this());

	}

}
