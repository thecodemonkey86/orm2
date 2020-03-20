package php.bean.method;

import php.bean.EntityCls;
import php.core.Attr;
import php.core.Param;
import php.core.Types;
import php.core.method.Method;
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
		EntityCls bean = (EntityCls) parent;
		Attr a = bean.getAttrByName("insertParamsForRawExpression"+col.getUc1stCamelCaseName());
		addInstr( a.arrayPush(param));
	}

}


