package cpp.beanquery.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.beanrepository.method.MethodFetchList;
import cpp.core.Method;
import cpp.core.expression.Expression;

public class MethodBeanQueryFetch extends Method{
	BeanCls bean;
	
	public MethodBeanQueryFetch(BeanCls bean) {
		super(Public, Types.qvector(bean.toSharedPtr()), "query");
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
		_return(aRepository.callMethod(MethodFetchList.getMethodName(bean),  _this().callMethod("execQuery")));
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
