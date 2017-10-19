package sunjava.cls.bean.repo.method;

import sunjava.cls.JavaCls;
import sunjava.cls.Method;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.bean.repo.query.ClsBeanQuery;
import sunjava.cls.expression.NewOperator;

public class MethodCreateQuery extends Method {
	BeanCls bean;
	
	public MethodCreateQuery(BeanCls cls) {
		//super(Public, new ClsBeanQuery(cls), "createQuery"+cls.getName());
		super(Public, new ClsBeanQuery(cls), "createQuery"+cls.getName());
//		setStatic(true);
		this.bean=cls;
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		JavaCls parent = (JavaCls) this.parent;
//		parent.addImport(((JavaCls)getReturnType()).getImport());
		//_return(new StdMoveExpression(new NewOperator(returnType, new NewOperator(new ClsBeanQuery(bean), parent.getAttrByName("sqlCon")) )));
		//_return(new MakeSharedExpression((SharedPtr)returnType, parent.getStaticAttribute("sqlCon").callMethod("buildQuery")));
		_return(new NewOperator(returnType,  parent.getAttrByName("sqlCon") ));
	}

}
