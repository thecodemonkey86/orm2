package php.cls.bean.repo.method;

import php.cls.PhpCls;
import php.cls.Method;
import php.cls.bean.BeanCls;
import php.cls.bean.repo.query.ClsBeanQuery;
import php.cls.expression.NewOperator;

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
		PhpCls parent = (PhpCls) this.parent;
//		parent.addImport(((PhpCls)getReturnType()).getImport());
		//_return(new StdMoveExpression(new NewOperator(returnType, new NewOperator(new ClsBeanQuery(bean), parent.getAttrByName("sqlCon")) )));
		//_return(new MakeSharedExpression((SharedPtr)returnType, parent.getStaticAttribute("sqlCon").callMethod("buildQuery")));
		_return(new NewOperator(returnType,  parent.getAttrByName("sqlCon") ));
	}

}
