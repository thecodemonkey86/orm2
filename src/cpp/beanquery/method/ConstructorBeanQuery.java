package cpp.beanquery.method;

import cpp.Types;
import cpp.core.Constructor;
import cpp.core.Param;
import cpp.core.expression.IntExpression;

public class ConstructorBeanQuery extends Constructor {
	Param pSqlCon;
	Param pRepository;
	
	public ConstructorBeanQuery() {
		super();
		pSqlCon = addParam( new Param(Types.Sql.toRawPointer(), "sqlCon"));
		pRepository = addParam(new Param(Types.BeanRepository.toSharedPtr(), "repository"));
	}

	@Override
	public void addImplementation() {
		addInstr( _this().assignAttr(pRepository.getName(), pRepository));
		addInstr( _this().assignAttr(pSqlCon.getName(), pSqlCon));
		addInstr( _this().assignAttr("limitResults", new IntExpression(0)));
		addInstr( _this().assignAttr("resultOffset", new IntExpression(-1)));
	}

	
	
}
