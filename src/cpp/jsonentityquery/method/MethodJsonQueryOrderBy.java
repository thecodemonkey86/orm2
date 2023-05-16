package cpp.jsonentityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.expression.BaseClassMethodCall;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.StaticCast;
import cpp.jsonentityquery.ClsJsonEntityQuerySelect;
import cpp.lib.ClsBaseJsonEntitySelectQuery;

public class MethodJsonQueryOrderBy extends Method{
	Param pExpr,pAsc;
	public MethodJsonQueryOrderBy(ClsJsonEntityQuerySelect cls) {
		super(Public, cls.toRef(), ClsBaseJsonEntitySelectQuery.orderBy);
		pExpr=addParam(Types.QString.toConstRef(),"expr");
		pAsc=addParam(Types.Bool,"asc",BoolExpression.TRUE);
		setVirtualQualifier(true);
		setOverrideQualifier(true);
	}

	@Override
	public void addImplementation() {
		_return(new StaticCast(parent.toRef(), new BaseClassMethodCall(parent.getSuperclass(), parent.getMethod(ClsBaseJsonEntitySelectQuery.orderBy),pExpr,pAsc)));
		
	}

}
