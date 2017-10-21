package cpp.beanrepository.method;

import cpp.bean.BeanCls;
import cpp.core.Method;
import cpp.core.SharedPtr;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.MakeSharedExpression;
import cpp.core.expression.Var;
import cpp.lib.EnableSharedFromThis;

public class MethodRepoCreateNew extends Method {

	BeanCls cls;
	
	public MethodRepoCreateNew(BeanCls cls) {
		super(Public, cls.toSharedPtr(), "createNew" + cls.getName());
		this.cls=cls;
	}

	@Override
	public void addImplementation() {
		Var bean = _declare(returnType, "bean", new MakeSharedExpression((SharedPtr) returnType,_this().callMethod(EnableSharedFromThis.SHARED_FROM_THIS)));
		_callMethodInstr(bean, "setInsertNew");
		addInstr(bean.callMethodInstruction("setLoaded", BoolExpression.TRUE));
		_return(bean);
		
	}

}
