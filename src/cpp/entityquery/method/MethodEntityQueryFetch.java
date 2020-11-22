package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.expression.InlineIfExpression;
import cpp.entity.EntityCls;
import cpp.entityquery.ClsEntityQuerySelect;
import cpp.entityrepository.method.MethodFetchList;
import cpp.util.ClsDbPool;

public class MethodEntityQueryFetch extends Method{
	protected EntityCls bean;
	protected Param pSqlCon;
	
	public MethodEntityQueryFetch(EntityCls bean) {
		super(Public, Types.qvector(bean.toSharedPtr()), "query");
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
		_return(bean.hasRelations() 
				? new InlineIfExpression(_this().accessAttr(ClsEntityQuerySelect.lazyLoading), Types.EntityRepository.callStaticMethod(MethodFetchList.getMethodName(bean,true),  _this().callMethod(MethodExecQuery.getMethodName(),pSqlCon)),Types.EntityRepository.callStaticMethod(MethodFetchList.getMethodName(bean,false),  _this().callMethod(MethodExecQuery.getMethodName(),pSqlCon)))
				: Types.EntityRepository.callStaticMethod(MethodFetchList.getMethodName(bean,false),  _this().callMethod(MethodExecQuery.getMethodName(),pSqlCon)));
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
