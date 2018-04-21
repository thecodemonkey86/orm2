package cpp.bean.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.core.Method;
import cpp.core.QString;

public class MethodGetTableName extends Method{

	
	public MethodGetTableName() {
		super(Method.Public, Types.QString, getMethodName());
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		BeanCls bean=(BeanCls) parent;
		_return(QString.fromStringConstant(bean.getTbl().getEscapedName()));
	}

	public static String getMethodName() {
		return "getTableName";
	}

}
