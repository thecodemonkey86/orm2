package cpp.entity.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.QString;
import cpp.entity.EntityCls;

public class MethodGetTableName extends Method{

	
	public MethodGetTableName() {
		super(Method.Public, Types.QString, getMethodName());
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		EntityCls bean=(EntityCls) parent;
		_return(QString.fromLatin1StringConstant(bean.getTbl().getEscapedName()));
	}

	public static String getMethodName() {
		return "getTableName";
	}

}
