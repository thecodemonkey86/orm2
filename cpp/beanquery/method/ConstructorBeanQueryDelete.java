package cpp.beanquery.method;

import cpp.Types;
import cpp.core.Constructor;
import cpp.core.Param;

public class ConstructorBeanQueryDelete extends Constructor {
	Param pSqlCon;
	Param pRepository;
	Param pTable;
	
	public ConstructorBeanQueryDelete() {
		super();
		pSqlCon = addParam( new Param(Types.Sql.toRawPointer(), "sqlCon"));
		pRepository = addParam(new Param(Types.BeanRepository.toSharedPtr(), "repository"));
		pTable = addParam(Types.QString.toConstRef(),"table");
	}

	@Override
	public void addImplementation() {
		addInstr( _this().assignAttr(pTable.getName(), pTable));
		addInstr( _this().assignAttr(pRepository.getName(), pRepository));
		addInstr( _this().assignAttr(pSqlCon.getName(), pSqlCon));
//		addInstr( _this().assignAttr("limitResults", new IntExpression(0)));
//		addInstr( _this().assignAttr("resultOffset", new IntExpression(-1)));
//		addInstr( _this().assignAttr(ClsBeanQuery.queryMode, EnumQueryMode.INSTANCE.constant(EnumQueryMode.Select)));
	}

	
	
}
