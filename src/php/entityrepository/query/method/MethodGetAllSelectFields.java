package php.entityrepository.query.method;

import php.core.Types;
import php.core.method.Method;
import php.entity.EntityCls;
import php.entityrepository.ClsEntityRepository;
import php.lib.ClsBaseEntityQuery;


public class MethodGetAllSelectFields extends Method {
	
	protected EntityCls bean;
	
	public MethodGetAllSelectFields(EntityCls cls) {
		super(Public, Types.String, "getAllSelectFields" );
		this.bean = cls;
	}

	@Override
	public void addImplementation() {
		_return(Types.EntityRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetAllSelectFields(bean), _this().accessAttr(ClsBaseEntityQuery.mainEntityAlias))); 
	}
	
	@Override
	public boolean includeIfEmpty() {
		return true;
	}

}
