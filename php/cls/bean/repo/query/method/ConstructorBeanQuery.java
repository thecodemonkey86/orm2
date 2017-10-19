package php.cls.bean.repo.query.method;

import php.cls.expression.NewOperator;
import php.Types;
import php.cls.Constructor;
import php.cls.Param;
import php.cls.bean.BeanCls;

public class ConstructorBeanQuery extends Constructor {

	public ConstructorBeanQuery() {
		super();
		Param sqlCon = new Param(Types.mysqli, "sqlCon");
		addParam(sqlCon);
		addPassToSuperConstructor(new NewOperator( BeanCls.getTypeMapper().getSqlQueryClass(), sqlCon));
	}

	@Override
	public void addImplementation() {
		// TODO Auto-generated method stub

	}

	
	
}
