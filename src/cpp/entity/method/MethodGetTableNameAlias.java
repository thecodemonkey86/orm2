package cpp.entity.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.entity.EntityCls;

public class MethodGetTableNameAlias extends Method{

	
	public MethodGetTableNameAlias() {
		super(Method.Public, Types.QString, "getTableName");
		addParam(new Param(Types.QString.toConstRef(), "alias"));
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		EntityCls entity=(EntityCls) parent;
		_return(QString.fromStringConstant(entity.getTbl().getEscapedName() +" %1").callMethod("arg", getParam("alias"))); 
		//_return(QString.fromExpression(aTableName).concat(QChar.fromChar(' ')).concat(QString.fromExpression(getParam("alias"))));
	}

}
