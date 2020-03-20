package php.beanrepository.query.method;

import php.bean.EntityCls;
import php.beanrepository.ClsBeanRepository;
import php.core.Types;
import php.core.method.Method;
import php.lib.ClsBaseEntityQuery;


public class MethodGetAllSelectFields extends Method {
	
	protected EntityCls bean;
	
	public MethodGetAllSelectFields(EntityCls cls) {
		super(Public, Types.String, "getAllSelectFields" );
		this.bean = cls;
	}

	@Override
	public void addImplementation() {
		_return(Types.BeanRepository.callStaticMethod(ClsBeanRepository.getMethodNameGetAllSelectFields(bean), _this().accessAttr(ClsBaseEntityQuery.mainBeanAlias))); 
	}
	
	@Override
	public boolean includeIfEmpty() {
		return true;
	}

}
