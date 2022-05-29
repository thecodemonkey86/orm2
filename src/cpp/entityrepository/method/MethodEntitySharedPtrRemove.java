package cpp.entityrepository.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.entity.EntityCls;

public class MethodEntitySharedPtrRemove extends Method {

	protected EntityCls bean;
	protected boolean overloadCascadeDeleteRelations;
	protected Param pBean;
	
	public MethodEntitySharedPtrRemove(EntityCls bean,
			 boolean overloadCascadeDeleteRelations
			) {
		super(Public, Types.Void, MethodEntityRemove.getMethodName());
		if (overloadCascadeDeleteRelations)
			this.addParam(new Param(Types.Bool, "overloadCascadeDeleteRelations"));
//		this.setVirtualQualifier(true);
		this.overloadCascadeDeleteRelations = overloadCascadeDeleteRelations;
		pBean = addParam(bean.toSharedPtr(), "entity");
		this.bean = bean;
	}

	

	@Override
	public void addImplementation() {
			 _callMethodInstr(_this(),MethodEntityRemove.getMethodName() , pBean.deref());

	}

}
