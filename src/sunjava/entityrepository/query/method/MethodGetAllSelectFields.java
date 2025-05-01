package sunjava.entityrepository.query.method;

import sunjava.core.Method;
import sunjava.core.Types;
import sunjava.entity.EntityCls;
import sunjava.entityrepository.ClsEntityRepository;
import sunjava.lib.ClsBaseEntityQuery;


public class MethodGetAllSelectFields extends Method {
	
	protected EntityCls entity;
	
	public MethodGetAllSelectFields(EntityCls cls) {
		super(Public, Types.String, "getAllSelectFields" );
		this.entity = cls;
		setOverrideAnnotation(true);
	}

	@Override
	public void addImplementation() {
		_return(Types.EntityRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetAllSelectFields(entity), _this().accessAttr(ClsBaseEntityQuery.MAIN_ENTITY_ALIAS))); 
	}
	
	@Override
	public boolean includeIfEmpty() {
		return true;
	}

}
