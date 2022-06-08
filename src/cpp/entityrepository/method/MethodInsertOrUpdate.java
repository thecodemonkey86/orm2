package cpp.entityrepository.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.entity.EntityCls;

public class MethodInsertOrUpdate extends Method {
	Param pEntity;
	public MethodInsertOrUpdate(EntityCls entity) {
		super(Public, Types.Void, "insertOrIgnore");
		pEntity = addParam(entity.toSharedPtr().toConstRef(),"entity");
	}

	@Override
	public void addImplementation() {
		addInstr(_this().callMethodInstruction(EntityCls.getDatabaseMapper().getRepositoryInsertOrUpdateMethod(), pEntity) ) ;

	}

}
