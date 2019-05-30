package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.expression.Expression;
import cpp.core.expression.InlineIfExpression;
import cpp.entity.EntityCls;
import cpp.entityquery.ClsEntityQuerySelect;
import cpp.entityrepository.method.MethodFetchList;

public class MethodEntityQueryFetch extends Method{
	EntityCls bean;
	
	public MethodEntityQueryFetch(EntityCls bean) {
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
		_return(new InlineIfExpression(_this().accessAttr(ClsEntityQuerySelect.lazyLoading), aRepository.callMethod(MethodFetchList.getMethodName(bean,true),  _this().callMethod("execQuery")),aRepository.callMethod(MethodFetchList.getMethodName(bean,false),  _this().callMethod("execQuery"))));
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
