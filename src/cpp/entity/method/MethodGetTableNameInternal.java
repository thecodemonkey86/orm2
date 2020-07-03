package cpp.entity.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.QString;
import cpp.entity.EntityCls;
@Deprecated
public class MethodGetTableNameInternal extends Method{

	
	public MethodGetTableNameInternal() {
		super(Method.Public, Types.QString, "getTableNameInternal");
	}

	@Override
	public void addImplementation() {
		//_return(QString.fromExpression(aTableName));
		EntityCls bean=(EntityCls) parent;
		_return(QString.fromLatin1StringConstant(bean.getTbl().getEscapedName()));
	}

}
