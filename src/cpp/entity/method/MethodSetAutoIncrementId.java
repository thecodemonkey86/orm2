package cpp.entity.method;

import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.entity.EntityCls;

public class MethodSetAutoIncrementId extends Method {

	public MethodSetAutoIncrementId( ) {
		super(Public, Types.Void, "setAutoIncrementId");
		
		addParam(new Param(Types.Int, "id"));
	}

	@Override
	public void addImplementation() {
		EntityCls bean = (EntityCls) parent;
		
		Attr attrAutoIncrement = bean.getAttrByName( bean.getTbl().getPrimaryKey().getAutoIncrementColumn().getCamelCaseName());
		addInstr( _this().accessAttr(attrAutoIncrement).assign( getParam("id")));
	}

}
