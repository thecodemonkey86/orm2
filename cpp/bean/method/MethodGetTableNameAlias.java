package cpp.bean.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;

public class MethodGetTableNameAlias extends Method{

	
	public MethodGetTableNameAlias() {
		super(Method.Public, Types.QString, "getTableName");
		addParam(new Param(Types.QString.toConstRef(), "alias"));
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		BeanCls bean=(BeanCls) parent;
		_return(QString.fromStringConstant(BeanCls.getDatabase().getEscapedTableName(bean.getTbl())+" %1").callMethod("arg", getParam("alias"))); 
		//_return(QString.fromExpression(aTableName).concat(QChar.fromChar(' ')).concat(QString.fromExpression(getParam("alias"))));
	}

}
