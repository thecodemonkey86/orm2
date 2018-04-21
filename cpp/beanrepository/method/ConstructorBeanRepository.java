package cpp.beanrepository.method;

import cpp.Types;
import cpp.core.Constructor;
import cpp.core.Param;

public class ConstructorBeanRepository extends Constructor {

	Param pSqlCon;
	
	public ConstructorBeanRepository() {
		pSqlCon = addParam(Types.Sql.toRawPointer(), "sqlCon");
	}
	
	@Override
	public void addImplementation() {
		addPassToSuperConstructor(pSqlCon);
		//addInstr(_this().assignAttr("sqlCon",getParam("sqlCon")));		
	}

}
