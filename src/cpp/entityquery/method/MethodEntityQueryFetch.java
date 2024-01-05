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
	protected EntityCls entity;
	protected Param pSqlCon;
	
	public MethodEntityQueryFetch(EntityCls entity) {
		super(Public, Types.qlist(entity.toSharedPtr()), "query");
		this.entity=entity;
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
		_return(entity.hasRelations() 
				? new InlineIfExpression(_this().accessAttr(ClsEntityQuerySelect.lazyLoading), Types.EntityRepository.callStaticMethod(MethodFetchList.getMethodName(entity,true),  _this().callMethod(MethodExecQuery.getMethodName(),pSqlCon)),Types.EntityRepository.callStaticMethod(MethodFetchList.getMethodName(entity,false),  _this().callMethod(MethodExecQuery.getMethodName(),pSqlCon)))
				: Types.EntityRepository.callStaticMethod(MethodFetchList.getMethodName(entity,false),  _this().callMethod(MethodExecQuery.getMethodName(),pSqlCon)));
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
