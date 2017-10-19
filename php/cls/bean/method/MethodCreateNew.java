package php.cls.bean.method;

import php.cls.Method;
import php.cls.bean.BeanCls;
import php.cls.expression.BoolExpression;
import php.cls.expression.NewOperator;
import php.cls.expression.Var;

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
