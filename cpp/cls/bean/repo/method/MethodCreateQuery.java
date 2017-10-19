package cpp.cls.bean.repo.method;

import cpp.cls.Method;
import cpp.cls.bean.BeanCls;
import cpp.cls.bean.repo.ClsBeanQuery;
import cpp.cls.expression.CreateObjectExpression;
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
