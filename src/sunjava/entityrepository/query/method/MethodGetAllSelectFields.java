package sunjava.entityrepository.query.method;

import sunjava.core.Method;
import sunjava.core.Types;
import sunjava.entity.EntityCls;
import sunjava.entityrepository.ClsEntityRepository;
import sunjava.lib.ClsBaseEntityQuery;


public class MethodGetAllSelectFields extends Method {
	
	protected EntityCls bean;
	
	public MethodGetAllSelectFields(EntityCls cls) {
		super(Public, Types.String, "getAllSelectFields" );
		this.bean = cls;
		setOverrideAnnotation(true);
	}

	@Override
	public void addImplementation() {
		_return(Types.BeanRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetAllSelectFields(bean), _this().accessAttr(ClsBaseEntityQuery.MAIN_ENTITY_ALIAS))); 
	}
	
	@Override
	public boolean includeIfEmpty() {
		return true;
	}

}
