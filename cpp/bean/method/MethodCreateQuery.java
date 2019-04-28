package cpp.bean.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.core.Method;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.NewOperator;
import cpp.core.expression.StdMoveExpression;

public class MethodCreateQuery extends Method {

	public MethodCreateQuery(BeanCls cls) {
		super(Public, Types.beanQuerySelect(cls).toUniquePointer(), "createQuery");
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		_return(new StdMoveExpression(new CreateObjectExpression(returnType, new NewOperator(Types.beanQuerySelect((BeanCls) parent), parent.getAttrByName("sqlCon").callMethod("buildQuery")) )));
		//_return(new MakeSharedExpression((SharedPtr)returnType, parent.getStaticAttribute("sqlCon").callMethod("buildQuery")));
	}

}
