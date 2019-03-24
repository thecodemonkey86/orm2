package php.bean.method;

import php.bean.BeanCls;
import php.core.Attr;
import php.core.Param;
import php.core.Types;
import php.core.method.Method;

public class MethodSetAutoIncrementId extends Method {

	public MethodSetAutoIncrementId( ) {
		super(Public, Types.Void, "setAutoIncrementId");
		
		addParam(new Param(Types.Int, "id"));
	}

	@Override
	public void addImplementation() {
		BeanCls bean = (BeanCls) parent;
		
		Attr attrAutoIncrement = bean.getAttrByName( bean.getTbl().getPrimaryKey().getAutoIncrementColumn().getCamelCaseName());
		addInstr( _this().accessAttr(attrAutoIncrement).assign( getParam("id")));
	}

}
