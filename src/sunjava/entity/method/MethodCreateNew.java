package sunjava.entity.method;

import sunjava.core.Method;
import sunjava.core.expression.BoolExpression;
import sunjava.core.expression.NewOperator;
import sunjava.core.expression.Var;
import sunjava.entity.EntityCls;

public class MethodCreateNew extends Method {

	EntityCls cls;
	
	public MethodCreateNew(EntityCls cls) {
		super(Public, cls, "createNew");
		setStatic(true);
		this.cls=cls;
	}

	@Override
	public void addImplementation() {
		Var bean = _declare(returnType, "entity", new NewOperator(cls
				));
		_callMethodInstr(bean, "setInsertNew");
		_assign(bean.accessAttr("loaded"), BoolExpression.TRUE);
		_return(bean);
		
	}

}
