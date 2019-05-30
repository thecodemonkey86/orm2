package cpp.entityquery.method;

import cpp.core.Method;
import cpp.core.expression.Expression;
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
		_return(aRepository.callMethod(MethodFetchOne.getMethodName(bean), _this().callMethod("execQuery")));
		//_return(_nullptr());
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
