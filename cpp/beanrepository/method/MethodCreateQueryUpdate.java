package cpp.beanrepository.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.core.Method;
import cpp.core.QString;
import cpp.core.expression.CreateObjectExpression;
import cpp.lib.EnableSharedFromThis;

public class MethodCreateQueryUpdate extends Method {
	BeanCls bean;
	
	public MethodCreateQueryUpdate(BeanCls cls) {
		//super(Public, new ClsBeanQuery(cls).toUniquePointer(), "createQuery"+cls.getName());
		super(Public, Types.beanQueryUpdate(cls),getMethodName(cls)
				);
//		setStatic(true);
		this.bean=cls;
	}

	@Override
	public void addImplementation() {
		//_return(new StdMoveExpression(new CreateObjectExpression(returnType, new NewOperator(new ClsBeanQuery(bean), parent.getAttrByName("sqlCon")) )));
		//_return(new MakeSharedExpression((SharedPtr)returnType, parent.getStaticAttribute("sqlCon").callMethod("buildQuery")));
		_return(new CreateObjectExpression(returnType,  parent.getAttrByName("sqlCon"), _this().callMethod(EnableSharedFromThis.SHARED_FROM_THIS),QString.fromStringConstant(bean.getTbl().getEscapedName()) ));
	}

	public static String getMethodName(BeanCls cls) {
		// TODO Auto-generated method stub
		return "update"+cls.getName();
	}

}
