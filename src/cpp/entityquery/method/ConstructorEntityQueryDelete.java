package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Constructor;
import cpp.core.Param;

public class ConstructorEntityQueryDelete extends Constructor {
	Param pTable;
	
	public ConstructorEntityQueryDelete() {
		super();
		pTable = addParam(Types.QString.toConstRef(),"table");
	}

	@Override
	public void addImplementation() {
		addInstr( _this().assignAttr(pTable.getName(), pTable));
	}

	
	
}
