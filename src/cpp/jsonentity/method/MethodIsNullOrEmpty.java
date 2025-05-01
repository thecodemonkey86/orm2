package cpp.jsonentity.method;

import cpp.CoreTypes;
import cpp.core.Method;
import cpp.core.expression.Expression;
import cpp.core.expression.Operators;
import cpp.core.Optional;
import cpp.lib.ClsQString;
import database.column.Column;

public class MethodIsNullOrEmpty extends Method{
	Column col;
	
	public MethodIsNullOrEmpty(Column c) {
		super(Public, CoreTypes.Bool, "is" +c.getUc1stCamelCaseName()+"NullOrEmpty");
		this.col = c;
		setConstQualifier();
	}

	@Override
	public void addImplementation() {
		Expression a = _this().accessAttr(col.getCamelCaseName());
		_return(_not(a.callMethod(Optional.has_value)).binOp(Operators.OR, a.callMethod(Optional.value).callMethod(ClsQString.isEmpty)));	
	}

}
