package cpp.jsonentityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.expression.BaseClassMethodCall;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.StaticCast;
import cpp.jsonentityquery.ClsJsonEntityQuerySelect;
import cpp.lib.ClsBaseJsonEntitySelectQuery;

public class MethodJsonQueryJoin extends Method{
	Param pTable,pOn,pParams;
	public MethodJsonQueryJoin(ClsJsonEntityQuerySelect cls) {
		super(Public, cls.toRef(), ClsBaseJsonEntitySelectQuery.join);
		pTable=addParam(Types.QString.toConstRef(),"table");
		pOn=addParam(Types.QString.toConstRef(),"on");
		pParams=addParam(Types.QVariantList.toConstRef(),"params",new CreateObjectExpression(Types.QVariantList));
		setVirtualQualifier(true);
		setOverrideQualifier(true);
	}

	@Override
	public void addImplementation() {
		_return(new StaticCast(parent.toRef(), new BaseClassMethodCall(parent.getSuperclass(), parent.getMethod(ClsBaseJsonEntitySelectQuery.join),pTable,pOn,pParams)));
		
	}

}
