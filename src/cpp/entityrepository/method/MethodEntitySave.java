package cpp.entityrepository.method;

import cpp.CoreTypes;
import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.entity.EntityCls;
import cpp.lib.ClsBaseRepository;
import cpp.util.ClsDbPool;

public class MethodEntitySave extends Method {

	Param pEntity;
	public MethodEntitySave(EntityCls entity) {
		super(Method.Public, CoreTypes.Void, "save");
		pEntity = addParam(entity.toSharedPtr().toConstRef(),"entity");
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		addInstr( Types.EntityRepository.callStaticMethod(ClsBaseRepository.save, pEntity, ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase)).asInstruction());
		
	}
}
