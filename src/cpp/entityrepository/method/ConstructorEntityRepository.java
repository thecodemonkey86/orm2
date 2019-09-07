package cpp.entityrepository.method;

import cpp.Types;
import cpp.core.Constructor;
import cpp.core.Param;

public class ConstructorEntityRepository extends Constructor {

	Param pSqlCon;
	
	public ConstructorEntityRepository() {
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(), "sqlCon");
	}
	
	@Override
	public void addImplementation() {
		addPassToSuperConstructor(pSqlCon);
		//addInstr(_this().assignAttr("sqlCon",getParam("sqlCon")));		
	}

}
