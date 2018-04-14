package cpp.beanrepository.method;

import cpp.bean.BeanCls;
import cpp.beanquery.ClsBeanQuery;
import cpp.core.Method;
import cpp.core.expression.CreateObjectExpression;
import cpp.lib.EnableSharedFromThis;

public class MethodCreateQuery extends Method {
	BeanCls bean;
	
	public MethodCreateQuery(BeanCls cls) {
		//super(Public, new ClsBeanQuery(cls).toUniquePointer(), "createQuery"+cls.getName());
		super(Public, new ClsBeanQuery(cls),getMethodName(cls)
				);
//		setStatic(true);
		this.bean=cls;
	}

	@Override
	public void addImplementation() {
		//_return(new StdMoveExpression(new CreateObjectExpression(returnType, new NewOperator(new ClsBeanQuery(bean), parent.getAttrByName("sqlCon")) )));
		//_return(new MakeSharedExpression((SharedPtr)returnType, parent.getStaticAttribute("sqlCon").callMethod("buildQuery")));
		_return(new CreateObjectExpression(returnType,  parent.getAttrByName("sqlCon"), _this().callMethod(EnableSharedFromThis.SHARED_FROM_THIS) ));
	}

	public static String getMethodName(BeanCls cls) {
		// TODO Auto-generated method stub
		return "createQuery"+cls.getName();
	}

}
