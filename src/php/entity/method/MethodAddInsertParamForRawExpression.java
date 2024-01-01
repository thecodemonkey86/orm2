package php.entity.method;

import php.core.Attr;
import php.core.Param;
import php.core.Types;
import php.core.method.Method;
import php.entity.EntityCls;
import database.column.Column;

public class MethodAddInsertParamForRawExpression extends Method {

	Column col;
	Param param;
	
	public MethodAddInsertParamForRawExpression(Column col) {
		super(Public, Types.Void, "addInsertParamForRawExpression"+col.getUc1stCamelCaseName());
		param = addParam(new Param(Types.Mixed, "param"));
		this.col = col;
	}

	@Override
	public void addImplementation() {
		EntityCls entity = (EntityCls) parent;
		Attr a = entity.getAttrByName("insertParamsForRawExpression"+col.getUc1stCamelCaseName());
		addInstr( a.arrayPush(param));
	}

}


