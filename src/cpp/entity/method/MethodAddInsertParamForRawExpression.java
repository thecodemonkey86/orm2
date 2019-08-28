package cpp.entity.method;

import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.entity.EntityCls;
import cpp.lib.ClsQVariantList;
import database.column.Column;

public class MethodAddInsertParamForRawExpression extends Method {

	Column col;
	Param param;
	
	public MethodAddInsertParamForRawExpression(Column col) {
		super(Public, Types.Void, "addInsertParamForRawExpression"+col.getUc1stCamelCaseName());
		param = addParam(new Param(Types.QVariant.toConstRef(), "param"));
		this.col = col;
	}

	@Override
	public void addImplementation() {
		EntityCls bean = (EntityCls) parent;
		Attr a = bean.getAttrByName("insertParamsForRawExpression"+col.getUc1stCamelCaseName());
		_callMethodInstr(a, ClsQVariantList.append, param);
	}

}
