package cpp.bean.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.core.Method;
import cpp.core.QString;

public class MethodGetTableNameInternal extends Method{

	
	public MethodGetTableNameInternal() {
		super(Method.Public, Types.QString, "getTableNameInternal");
	}

	@Override
	public void addImplementation() {
		//_return(QString.fromExpression(aTableName));
		BeanCls bean=(BeanCls) parent;
		_return(QString.fromStringConstant(BeanCls.getDatabase().getEscapedTableName(bean.getTbl())));
	}

}
