package php.entity.method;

import php.core.expression.BoolExpression;
import php.core.expression.NewOperator;
import php.core.expression.Var;
import php.core.method.Method;
import php.entity.EntityCls;

public class MethodCreateNew extends Method {

	EntityCls cls;
	
	public MethodCreateNew(EntityCls cls) {
		super(Public, cls,getMethodName());
//		addParam(new Param(Types.Sql, "sqlCon"));
		setStatic(true);
		this.cls=cls;
	}

	@Override
	public void addImplementation() {
		Var bean = _declare(returnType, "entity", new NewOperator(cls
//				,getParam("sqlCon")
				));
		_callMethodInstr(bean, "setInsertNew");
		_assign(bean.accessAttr("loaded"), BoolExpression.TRUE);
		_return(bean);
		
	}

	public static String getMethodName() {
		return  "createNew";
	}
}
