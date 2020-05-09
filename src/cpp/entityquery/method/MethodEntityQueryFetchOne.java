package cpp.entityquery.method;

import cpp.core.Method;
import cpp.core.expression.Expression;
import cpp.core.instruction.IfBlock;
import cpp.entity.EntityCls;
import cpp.entityrepository.method.MethodFetchOne;

public class MethodEntityQueryFetchOne extends Method{
	EntityCls bean;
	
	public MethodEntityQueryFetchOne(EntityCls bean) {
		super(Public, bean.toSharedPtr(), "queryOne");
		this.bean=bean;
	}

	@Override
	public void addImplementation() {
//		addInstr(new Instruction() {
//			@Override
//			public String toString() {
//				return "qDebug()<<qu->toString();";
//			}
//		});
		Expression aRepository = _this().accessAttr("repository");
		
		if(bean.hasRelations()) {
			IfBlock ifLazyLoading = _if(_this().accessAttr("lazyLoading"));
			
			ifLazyLoading.thenBlock().
			_return(aRepository.callMethod(MethodFetchOne.getMethodName(bean, true), _this().callMethod("execQuery")));
			ifLazyLoading.elseBlock().
			_return(aRepository.callMethod(MethodFetchOne.getMethodName(bean, false), _this().callMethod("execQuery")));
		} else {
			_return(aRepository.callMethod(MethodFetchOne.getMethodName(bean, false), _this().callMethod("execQuery")));
		}
			
		
		
		//_return(_nullptr());
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}