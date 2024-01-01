package php.entityrepository.query.method;

import php.core.Types;
import php.core.method.Method;
import php.entity.EntityCls;
import php.entityrepository.ClsEntityRepository;
import php.lib.ClsBaseEntityQuery;


public class MethodGetAllSelectFields extends Method {
	
	protected EntityCls entity;
	
	public MethodGetAllSelectFields(EntityCls cls) {
		super(Public, Types.String, "getAllSelectFields" );
		this.entity = cls;
	}

	@Override
	public void addImplementation() {
		_return(Types.EntityRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetAllSelectFields(entity), _this().accessAttr(ClsBaseEntityQuery.mainEntityAlias))); 
	}
	
	@Override
	public boolean includeIfEmpty() {
		return true;
	}

}
