package cpp.beanquery.method;

import cpp.bean.BeanCls;
import cpp.beanrepository.method.MethodFetchOne;
import cpp.core.Method;
import cpp.core.expression.Expression;

public class MethodBeanQueryFetchOne extends Method{
	BeanCls bean;
	
	public MethodBeanQueryFetchOne(BeanCls bean) {
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
