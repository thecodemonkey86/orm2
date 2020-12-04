package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.instruction.IfBlock;
import cpp.entity.EntityCls;
import cpp.entityrepository.method.MethodFetchOne;
import cpp.util.ClsDbPool;

public class MethodEntityQueryFetchOne extends Method{
	protected EntityCls bean;
	protected Param pSqlCon;
	
	public MethodEntityQueryFetchOne(EntityCls bean) {
		super(Public, bean.toSharedPtr(), "queryOne");
		this.bean=bean;
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
		
		if(bean.hasRelations()) {
			IfBlock ifLazyLoading = _if(_this().accessAttr("lazyLoading"));
			
			ifLazyLoading.thenBlock().
			_return(Types.EntityRepository.callStaticMethod(MethodFetchOne.getMethodName(bean, true), _this().callMethod(MethodExecQuery.getMethodName(),pSqlCon)));
			ifLazyLoading.elseBlock().
			_return(Types.EntityRepository.callStaticMethod(MethodFetchOne.getMethodName(bean, false), _this().callMethod(MethodExecQuery.getMethodName(),pSqlCon)));
		} else {
			_return(Types.EntityRepository.callStaticMethod(MethodFetchOne.getMethodName(bean, false), _this().callMethod(MethodExecQuery.getMethodName(),pSqlCon)));
		}
			
		
		
		//_return(_nullptr());
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
