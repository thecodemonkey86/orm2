package php.entity.method;

import php.core.PhpCls;
import php.core.expression.NewOperator;
import php.core.method.Method;
import php.entity.EntityCls;


public class MethodCreateQuery extends Method {

	public MethodCreateQuery(EntityCls cls) {
		super(Public, EntityCls.getTypeMapper().getBeanQueryClass(cls), "createQuery");
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		PhpCls parent = (PhpCls) this.parent;
		_return(new NewOperator(returnType, new NewOperator(returnType, parent.getAttrByName("sqlCon").callMethod("buildQuery")) ));
		//_return(new MakeSharedExpression((SharedPtr)returnType, parent.getStaticAttribute("sqlCon").callMethod("buildQuery")));
	}

}
