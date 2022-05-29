package cpp.entityrepository.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.expression.StaticMethodCall;
import cpp.entity.EntityCls;
import cpp.util.ClsDbPool;

public class MethodEntitySharedPtrRemove extends Method {

	protected EntityCls bean;
	protected boolean overloadCascadeDeleteRelations;
	protected Param pBean;
	protected Param pSqlCon;
	
	public MethodEntitySharedPtrRemove(EntityCls bean,
			 boolean overloadCascadeDeleteRelations
			) {
		super(Public, Types.Void, MethodEntityRemove.getMethodName());
		if (overloadCascadeDeleteRelations)
			this.addParam(new Param(Types.Bool, "overloadCascadeDeleteRelations"));
//		this.setVirtualQualifier(true);
		this.overloadCascadeDeleteRelations = overloadCascadeDeleteRelations;
		pBean = addParam(bean.toSharedPtr().toConstRef(), "entity");
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(),"sqlCon",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase));
		this.bean = bean;
		setStatic(true);
	}

	

	@Override
	public void addImplementation() {
		 addInstr(new StaticMethodCall(parent, parent.getMethod(MethodEntityRemove.getMethodName()), pBean.deref(),pSqlCon).asInstruction());

	}

}
