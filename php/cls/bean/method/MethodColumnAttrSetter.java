package php.cls.bean.method;

import model.Column;
import php.cls.Attr;
import php.cls.Method;
import php.cls.Param;
import php.cls.bean.BeanCls;
import php.cls.expression.BoolExpression;
import php.cls.expression.Expression;
import php.cls.expression.Expressions;
import php.cls.instruction.AssignInstruction;
import php.cls.instruction.IfBlock;
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
