package cpp.cls.bean.repo.method;

import cpp.Types;
import cpp.cls.Constructor;
import cpp.cls.Param;

public class ConstructorBeanRepository extends Constructor {

	public ConstructorBeanRepository() {
		addParam(new Param(Types.Sql.toRawPointer(), "sqlCon"));
	}
	
	@Override
	public void addImplementation() {
		addPassToSuperConstructor(getParam("sqlCon"));
		//addInstr(_this().assignAttr("sqlCon",getParam("sqlCon")));		
	}

}
