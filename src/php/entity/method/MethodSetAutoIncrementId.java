package php.entity.method;

import php.core.Attr;
import php.core.Param;
import php.core.Types;
import php.core.method.Method;
import php.entity.EntityCls;

public class MethodSetAutoIncrementId extends Method {

	public MethodSetAutoIncrementId( ) {
		super(Public, Types.Void, "setAutoIncrementId");
		
		addParam(new Param(Types.Int, "id"));
	}

	@Override
	public void addImplementation() {
		EntityCls entity = (EntityCls) parent;
		
		Attr attrAutoIncrement = entity.getAttrByName( entity.getTbl().getPrimaryKey().getAutoIncrementColumn().getCamelCaseName());
		addInstr( _this().accessAttr(attrAutoIncrement).assign( getParam("id")));
	}

}
