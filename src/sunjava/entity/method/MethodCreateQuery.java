package sunjava.entity.method;

import sunjava.core.JavaCls;
import sunjava.core.Method;
import sunjava.core.expression.NewOperator;
import sunjava.entity.EntityCls;


public class MethodCreateQuery extends Method {

	public MethodCreateQuery(EntityCls cls) {
		super(Public, EntityCls.getTypeMapper().getBeanQueryClass(cls), "createQuery");
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		JavaCls parent = (JavaCls) this.parent;
		_return(new NewOperator(returnType, new NewOperator(returnType, parent.getAttrByName("sqlCon").callMethod("buildQuery")) ));
		//_return(new MakeSharedExpression((SharedPtr)returnType, parent.getStaticAttribute("sqlCon").callMethod("buildQuery")));
	}

}
