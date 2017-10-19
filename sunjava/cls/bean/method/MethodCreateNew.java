package sunjava.cls.bean.method;

import sunjava.cls.Method;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.expression.BoolExpression;
import sunjava.cls.expression.NewOperator;
import sunjava.cls.expression.Var;

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
		Var bean = _declare(returnType, "bean", new NewOperator(cls
//				,getParam("sqlCon")
				));
		_callMethodInstr(bean, "setInsertNew");
		_assign(bean.accessAttr("loaded"), BoolExpression.TRUE);
		_return(bean);
		
	}

}
