package cpp.cls.bean.method;

import cpp.Types;
import cpp.cls.Method;
import cpp.cls.QString;
import cpp.cls.bean.BeanCls;

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
