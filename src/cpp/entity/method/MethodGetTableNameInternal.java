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
		EntityCls entity=(EntityCls) parent;
		_return(QString.fromStringConstant(entity.getTbl().getEscapedName()));
	}

}
