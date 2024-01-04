package php.entityrepository.query.method;

import php.core.Constructor;
import php.core.Param;
import php.core.expression.NewOperator;
import php.entity.EntityCls;

public class ConstructorEntityQuery extends Constructor {

	public ConstructorEntityQuery() {
		super();
		Param sqlCon = new Param(EntityCls.getTypeMapper().getDatabaseLinkType(), "sqlCon");
		addParam(sqlCon);
		addPassToSuperConstructor(new NewOperator( EntityCls.getTypeMapper().getSqlQueryClass(), sqlCon));
	}

	@Override
	public void addImplementation() {
		// TODO Auto-generated method stub

	}

	
	
}
