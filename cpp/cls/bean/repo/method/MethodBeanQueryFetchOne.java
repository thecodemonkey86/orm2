package cpp.cls.bean.repo.method;

import cpp.cls.Method;
import cpp.cls.bean.BeanCls;
import cpp.cls.expression.Expression;

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
