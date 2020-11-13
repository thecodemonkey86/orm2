package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Constructor;
import cpp.core.Param;

public class ConstructorEntityQueryUpdate extends Constructor {
	Param pTable;
	public ConstructorEntityQueryUpdate() {
		super();
		pTable = addParam(new Param(Types.QString.toConstRef(), "table"));
	}

	@Override
	public void addImplementation() {
		addInstr( _this().assignAttr(pTable.getName(), pTable));
		//addInstr( _this().assignAttr("limitResults", new IntExpression(0)));
		//addInstr( _this().assignAttr("resultOffset", new IntExpression(-1)));
//		addInstr( _this().assignAttr(ClsBeanQuery.queryMode, EnumQueryMode.INSTANCE.constant(EnumQueryMode.Select)));
	}

	
	
}
