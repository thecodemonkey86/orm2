package cpp.cls.bean.repo.method;

import cpp.cls.Method;
import cpp.cls.SharedPtr;
import cpp.cls.bean.BeanCls;
import cpp.cls.expression.BoolExpression;
import cpp.cls.expression.MakeSharedExpression;
import cpp.cls.expression.Var;
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
