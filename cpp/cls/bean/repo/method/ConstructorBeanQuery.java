package cpp.cls.bean.repo.method;

import cpp.Types;
import cpp.cls.Constructor;
import cpp.cls.Param;

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
