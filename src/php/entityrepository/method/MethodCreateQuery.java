package php.entityrepository.method;

import php.core.PhpCls;
import php.core.expression.NewOperator;
import php.core.method.Method;
import php.entity.EntityCls;
import php.entityrepository.query.ClsEntityQuery;

public class MethodCreateQuery extends Method {
	EntityCls entity;
	
	public MethodCreateQuery(EntityCls cls) {
		//super(Public, new ClsBeanQuery(cls), "createQuery"+cls.getName());
		super(Public, new ClsEntityQuery(cls), getMethodName(cls));
//		setStatic(true);
		this.entity=cls;
		setStatic(true);
	}

	public static String getMethodName(EntityCls cls) {
		return "createQuery"+cls.getName();
	}

	@Override
	public void addImplementation() {
		PhpCls parent = (PhpCls) this.parent;
//		parent.addImport(((PhpCls)getReturnType()).getImport());
		//_return(new StdMoveExpression(new NewOperator(returnType, new NewOperator(new ClsBeanQuery(entity), parent.getAttrByName("sqlCon")) )));
		//_return(new MakeSharedExpression((SharedPtr)returnType, parent.getStaticAttribute("sqlCon").callMethod("buildQuery")));
		_return(new NewOperator(returnType,  parent.getAttrByName("sqlCon") ));
	}

}
