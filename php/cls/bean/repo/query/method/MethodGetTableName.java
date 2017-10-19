package php.cls.bean.repo.query.method;

import php.Types;
import php.cls.Method;
import php.cls.bean.BeanCls;
import php.cls.expression.PhpStringLiteral;


public class MethodGetTableName extends Method {
	BeanCls cls;
	
	public MethodGetTableName(BeanCls cls) {
		super(Public, Types.String, "getTableName");
		this.cls = cls;
	}

	@Override
	public void addImplementation() {
		_return(new PhpStringLiteral(cls.getTbl().getName()));
		
	}

}
