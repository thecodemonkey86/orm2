package cpp.cls.bean.repo.method;

import cpp.Types;
import cpp.cls.Method;
import cpp.cls.bean.BeanCls;
import cpp.cls.expression.Expression;

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
