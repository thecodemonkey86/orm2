package sunjava.cls.bean.repo.query.method;

import sunjava.cls.expression.NewOperator;
import sunjava.Types;
import sunjava.cls.Constructor;
import sunjava.cls.Param;
import sunjava.cls.bean.BeanCls;

public class ConstructorBeanQuery extends Constructor {

	public ConstructorBeanQuery() {
		super();
		Param sqlCon = new Param(Types.Connection, "sqlCon");
		addParam(sqlCon);
		addPassToSuperConstructor(new NewOperator( BeanCls.getTypeMapper().getSqlQueryClass(), sqlCon));
	}

	@Override
	public void addImplementation() {
		// TODO Auto-generated method stub

	}

	
	
}
