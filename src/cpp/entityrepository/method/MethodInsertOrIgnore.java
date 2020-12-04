package cpp.entityrepository.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.entity.EntityCls;

public class MethodInsertOrIgnore extends Method {
	Param pEntity;
	public MethodInsertOrIgnore(EntityCls entity) {
		super(Public, Types.Void, "insertOrIgnore");
		pEntity = addParam(entity.toSharedPtr().toConstRef(),"entity");
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		addInstr(parent.callStaticMethod(EntityCls.getDatabaseMapper().getRepositoryInsertOrIgnoreMethod(), pEntity).asInstruction() ) ;

	}

}
