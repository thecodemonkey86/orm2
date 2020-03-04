package cpp.entityrepository.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.entity.EntityCls;
import cpp.lib.ClsBaseRepository;

public class MethodEntitySave extends Method {

//	protected boolean overloadCascadeSaveRelations;
	protected EntityCls bean;
	protected Param pBean;
	protected boolean upsert;
	
	public MethodEntitySave(EntityCls bean,boolean upsert
//			, boolean overloadCascadeSaveRelations
			) {
		super(Public, Types.Void, upsert?"insertOrIgnore": "save");
//		if (!overloadCascadeSaveRelations)
//			this.addParam(new Param(Types.Bool, "cascadeSaveRelations"));
//		this.setVirtualQualifier(true);
//		this.overloadCascadeSaveRelations = overloadCascadeSaveRelations;
		pBean = addParam(bean.toSharedPtr().toConstRef(), "entity");
		this.bean = bean;
		this.upsert = upsert;
		
	}

	

	@Override
	public void addImplementation() {
//		if (overloadCascadeSaveRelations) {
//			addInstr(new StaticMethodCall(bean, parent.getMethod("save"), BoolExpression.FALSE).asInstruction()) ;
//			
//		} else {
//			addInstr(new StaticMethodCall(parent.getSuperclass(), parent.getMethod("save" ), getParam("cascadeSaveRelations")).asInstruction()) ;
			
			if(upsert) {
				addInstr(_this().callMethodInstruction(EntityCls.getDatabaseMapper().getRepositoryInsertOrIgnoreMethod(),pBean)) ;
			} else {
				addInstr(_this().callMethodInstruction(ClsBaseRepository.saveBean,pBean));
			}
		
			 

	}

}
