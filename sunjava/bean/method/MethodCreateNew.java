package sunjava.bean.method;

import sunjava.bean.BeanCls;
import sunjava.core.Method;
import sunjava.core.expression.BoolExpression;
import sunjava.core.expression.NewOperator;
import sunjava.core.expression.Var;

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
