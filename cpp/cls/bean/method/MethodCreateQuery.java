package cpp.cls.bean.method;

import cpp.Types;
import cpp.cls.Cls;
import cpp.cls.Method;
import cpp.cls.bean.BeanCls;
import cpp.cls.expression.CreateObjectExpression;
import cpp.cls.expression.NewOperator;
import cpp.cls.expression.StdMoveExpression;

public class MethodCreateQuery extends Method {

	public MethodCreateQuery(BeanCls cls) {
		super(Public, Types.beanQuery(cls).toUniquePointer(), "createQuery");
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		_return(new StdMoveExpression(new CreateObjectExpression(returnType, new NewOperator(Types.beanQuery((Cls)parent), parent.getAttrByName("sqlCon").callMethod("buildQuery")) )));
		//_return(new MakeSharedExpression((SharedPtr)returnType, parent.getStaticAttribute("sqlCon").callMethod("buildQuery")));
	}

}
