package sunjava.entityrepository.query.method;

import sunjava.core.Constructor;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.expression.NewOperator;
import sunjava.entity.EntityCls;

public class ConstructorBeanQuery extends Constructor {

	public ConstructorBeanQuery() {
		super();
		Param sqlCon = new Param(Types.Connection, "sqlCon");
		addParam(sqlCon);
		addPassToSuperConstructor(new NewOperator( EntityCls.getTypeMapper().getSqlQueryClass(), sqlCon));
	}

	@Override
	public void addImplementation() {
		// TODO Auto-generated method stub

	}

	
	
}
