package php.bean.method;

import php.bean.BeanCls;
import php.core.expression.BoolExpression;
import php.core.expression.NewOperator;
import php.core.expression.Var;
import php.core.method.Method;

public class MethodCreateNew extends Method {

	BeanCls cls;
	
	public MethodCreateNew(BeanCls cls) {
		super(Public, cls, "createNew");
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

}
