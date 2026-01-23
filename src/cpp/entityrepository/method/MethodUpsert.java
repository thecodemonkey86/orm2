package cpp.entityrepository.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.entity.EntityCls;

public class MethodUpsert extends Method {
	Param pEntity;
	boolean byRef;
	public MethodUpsert(EntityCls entity,boolean byRef) {
		super(Public, Types.Void, "upsert");
		this.byRef=byRef;
		pEntity = addParam( byRef?entity.toRef():  entity.toSharedPtr().toConstRef(),"entity");
	}

	@Override
	public void addImplementation() {
		addInstr(_this().callMethodInstruction(EntityCls.getDatabaseMapper().getRepositoryInsertOrUpdateMethod(),this.byRef?pEntity: pEntity.deref()) ) ;

	}

}
