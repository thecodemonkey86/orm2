package php.beanrepository.query.method;

import php.core.Constructor;
import php.core.Param;
import php.core.Types;
import php.core.expression.NewOperator;
import php.bean.BeanCls;

public class ConstructorBeanQuery extends Constructor {

	public ConstructorBeanQuery() {
		super();
		Param sqlCon = new Param(BeanCls.getTypeMapper().getDatabaseLinkType(), "sqlCon");
		addParam(sqlCon);
		addPassToSuperConstructor(new NewOperator( BeanCls.getTypeMapper().getSqlQueryClass(), sqlCon));
	}

	@Override
	public void addImplementation() {
		// TODO Auto-generated method stub

	}

	
	
}
