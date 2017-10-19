package php.cls.bean.method;

import php.Types;
import php.cls.Attr;
import php.cls.Method;
import php.cls.Param;
import php.cls.bean.BeanCls;

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
