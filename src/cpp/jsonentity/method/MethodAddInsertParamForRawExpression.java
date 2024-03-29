package cpp.jsonentity.method;

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
		EntityCls entity = (EntityCls) parent;
		Attr a = entity.getAttrByName("insertParamsForRawExpression"+col.getUc1stCamelCaseName());
		_callMethodInstr(a, ClsQVariantList.append, param);
	}

}
