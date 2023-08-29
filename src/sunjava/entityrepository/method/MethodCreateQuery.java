package sunjava.entityrepository.method;

import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.expression.NewOperator;
import sunjava.entity.EntityCls;
import sunjava.entityrepository.query.ClsEntityQuery;

public class MethodCreateQuery extends Method {
	EntityCls bean;
	Param pSqlCon;
	public MethodCreateQuery(EntityCls cls) {
		//super(Public, new ClsBeanQuery(cls), "createQuery"+cls.getName());
		super(Public, new ClsEntityQuery(cls), "createQuery"+cls.getName());
//		setStatic(true);
		this.bean=cls;
		pSqlCon =addParam(new Param(Types.Connection, "sqlConnection"));
		setStatic(true);
	}

	@Override
	public void addImplementation() {
//		JavaCls parent = (JavaCls) this.parent;
//		parent.addImport(((JavaCls)getReturnType()).getImport());
		//_return(new StdMoveExpression(new NewOperator(returnType, new NewOperator(new ClsBeanQuery(bean), parent.getAttrByName("sqlCon")) )));
		//_return(new MakeSharedExpression((SharedPtr)returnType, parent.getStaticAttribute("sqlCon").callMethod("buildQuery")));
		_return(new NewOperator(returnType,  pSqlCon) );
	}

}
