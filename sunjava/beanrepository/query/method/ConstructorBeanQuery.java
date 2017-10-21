package sunjava.beanrepository.query.method;

import sunjava.bean.BeanCls;
import sunjava.core.Constructor;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.expression.NewOperator;

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
