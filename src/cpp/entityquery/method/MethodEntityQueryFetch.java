package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.expression.Expression;
import cpp.core.expression.InlineIfExpression;
import cpp.entity.EntityCls;
import cpp.entityquery.ClsEntityQuerySelect;
import cpp.entityrepository.method.MethodFetchList;

public class MethodEntityQueryFetch extends Method{
	private static final String execQuery = "execQuery";
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
		_return(bean.hasRelations() ? new InlineIfExpression(_this().accessAttr(ClsEntityQuerySelect.lazyLoading), Types.EntityRepository.callStaticMethod(MethodFetchList.getMethodName(bean,true),  _this().callMethod(execQuery)),Types.EntityRepository.callStaticMethod(MethodFetchList.getMethodName(bean,false),  _this().callMethod(execQuery))):Types.EntityRepository.callStaticMethod(MethodFetchList.getMethodName(bean,false),  _this().callMethod(execQuery)));
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
