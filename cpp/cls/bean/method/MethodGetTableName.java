package cpp.cls.bean.method;

import cpp.Types;
import cpp.cls.Method;
import cpp.cls.QString;
import cpp.cls.bean.BeanCls;

public class MethodGetTableName extends Method{

	
	public MethodGetTableName() {
		super(Method.Public, Types.QString, "getTableName");
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		BeanCls bean=(BeanCls) parent;
		_return(QString.fromStringConstant(bean.getTbl().getName()));
	}

}
