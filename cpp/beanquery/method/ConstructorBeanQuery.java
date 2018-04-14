package cpp.beanquery.method;

import cpp.Types;
import cpp.core.Constructor;
import cpp.core.Param;

public class ConstructorBeanQuery extends Constructor {

	public ConstructorBeanQuery() {
		super();
		Param sqlCon = new Param(Types.Sql.toRawPointer(), "sqlCon");
		addParam(sqlCon);
		addParam(new Param(Types.BeanRepository.toSharedPtr(), "repository"));
		addPassToSuperConstructor(sqlCon);
	}

	@Override
	public void addImplementation() {
		Param pRepository = getParam("repository");
		addInstr( _this().assignAttr(pRepository.getName(), pRepository));

	}

	
	
}
