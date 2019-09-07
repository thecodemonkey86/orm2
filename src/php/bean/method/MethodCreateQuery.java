package php.bean.method;

import php.bean.BeanCls;
import php.core.PhpCls;
import php.core.expression.NewOperator;
import php.core.method.Method;


public class MethodCreateQuery extends Method {

	public MethodCreateQuery(BeanCls cls) {
		super(Public, BeanCls.getTypeMapper().getBeanQueryClass(cls), "createQuery");
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		PhpCls parent = (PhpCls) this.parent;
		_return(new NewOperator(returnType, new NewOperator(returnType, parent.getAttrByName("sqlCon").callMethod("buildQuery")) ));
		//_return(new MakeSharedExpression((SharedPtr)returnType, parent.getStaticAttribute("sqlCon").callMethod("buildQuery")));
	}

}
