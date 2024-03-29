package sunjava.entity.method;

import sunjava.core.Attr;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.entity.EntityCls;

public class MethodSetAutoIncrementId extends Method {

	public MethodSetAutoIncrementId( ) {
		super(Public, Types.Void, "setAutoIncrementId");
		
		addParam(new Param(Types.Int, "id"));
		setOverrideAnnotation(true);
	}

	@Override
	public void addImplementation() {
		EntityCls entity = (EntityCls) parent;
		
		Attr attrAutoIncrement = entity.getAttrByName( entity.getTbl().getPrimaryKey().getAutoIncrementColumn().getCamelCaseName());
		addInstr( _this().accessAttr(attrAutoIncrement).assign( getParam("id")));
	}

}
