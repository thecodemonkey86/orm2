package cpp.beanquery.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.beanquery.ClsBeanQuerySelect;
import cpp.beanrepository.method.MethodFetchList;
import cpp.core.Method;
import cpp.core.expression.Expression;
import cpp.core.expression.InlineIfExpression;

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
		_return(new InlineIfExpression(_this().accessAttr(ClsBeanQuerySelect.lazyLoading), aRepository.callMethod(MethodFetchList.getMethodName(bean,true),  _this().callMethod("execQuery")),aRepository.callMethod(MethodFetchList.getMethodName(bean,false),  _this().callMethod("execQuery"))));
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
