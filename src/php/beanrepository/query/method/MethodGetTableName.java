package php.beanrepository.query.method;

import php.bean.EntityCls;
import php.core.Types;
import php.core.expression.PhpStringLiteral;
import php.core.method.Method;


public class MethodGetTableName extends Method {
	EntityCls cls;
	
	public MethodGetTableName(EntityCls cls) {
		super(Public, Types.String, "getTableName");
		this.cls = cls;
	}

	@Override
	public void addImplementation() {
		_return(new PhpStringLiteral(cls.getTbl().getName()));
		
	}

}
