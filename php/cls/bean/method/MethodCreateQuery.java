package php.cls.bean.method;

import php.cls.PhpCls;
import php.cls.Method;
import php.cls.bean.BeanCls;
import php.cls.expression.NewOperator;


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
