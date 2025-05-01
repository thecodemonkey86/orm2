package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.IfBlock;
import cpp.entity.EntityCls;
import cpp.entityrepository.method.MethodFetchOne;
import cpp.util.ClsDbPool;

public class MethodEntityQueryFetchOne extends Method{
	protected EntityCls entity;
	protected Param pSqlCon;
	
	public static String METHOD_NAME="queryOne";
	
	public MethodEntityQueryFetchOne(EntityCls entity) {
		super(Public, entity.toSharedPtr(),METHOD_NAME );
		this.entity=entity;
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(),"sqlCon",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase));
	}

	@Override
	public void addImplementation() {
//		addInstr(new Instruction() {
//			@Override
//			public String toString() {
//				return "qDebug()<<qu->toString();";
//			}
//		});
		
		if(entity.hasRelations()) {
			IfBlock ifLazyLoading = _if(_this().accessAttr("lazyLoading"));
			
			ifLazyLoading.thenBlock().
			_return(Types.EntityRepository.callStaticMethod(MethodFetchOne.getMethodName(entity, true), _this().callMethod(MethodExecQuery.getMethodName(),pSqlCon)));
			ifLazyLoading.elseBlock().
			_return(Types.EntityRepository.callStaticMethod(MethodFetchOne.getMethodName(entity, false), _this().callMethod(MethodExecQuery.getMethodName(),pSqlCon)));
		} else {
			_return(Types.EntityRepository.callStaticMethod(MethodFetchOne.getMethodName(entity, false), _this().callMethod(MethodExecQuery.getMethodName(),pSqlCon)));
		}
			
		
		
		//_return(_nullptr());
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
