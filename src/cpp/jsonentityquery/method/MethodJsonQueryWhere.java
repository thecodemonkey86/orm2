package cpp.jsonentityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.expression.BaseClassMethodCall;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.StaticCast;
import cpp.lib.ClsBaseJsonEntitySelectQuery;

public class MethodJsonQueryWhere extends Method{
	Param pCond,pParams;
	public MethodJsonQueryWhere(Cls  cls) {
		super(Public, cls.toRef(), ClsBaseJsonEntitySelectQuery.where);
		pCond=addParam(Types.QString.toConstRef(),"cond");
		pParams=addParam(Types.QVariantList.toConstRef(),"params",new CreateObjectExpression(Types.QVariantList));
		setVirtualQualifier(true);
		setOverrideQualifier(true);
	}

	@Override
	public void addImplementation() {
		_return(new StaticCast(parent.toRef(), new BaseClassMethodCall(parent.getSuperclass(), parent.getMethod(ClsBaseJsonEntitySelectQuery.where),pCond,pParams)));
		
	}

}
