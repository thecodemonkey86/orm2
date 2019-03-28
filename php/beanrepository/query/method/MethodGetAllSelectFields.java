package php.beanrepository.query.method;

import php.bean.BeanCls;
import php.beanrepository.ClsBeanRepository;
import php.core.Types;
import php.core.method.Method;
import php.lib.ClsBaseBeanQuery;


public class MethodGetAllSelectFields extends Method {
	
	protected BeanCls bean;
	
	public MethodGetAllSelectFields(BeanCls cls) {
		super(Public, Types.String, "getAllSelectFields" );
		this.bean = cls;
	}

	@Override
	public void addImplementation() {
		_return(Types.BeanRepository.callStaticMethod(ClsBeanRepository.getMethodNameGetAllSelectFields(bean), _this().accessAttr(ClsBaseBeanQuery.mainBeanAlias))); 
	}
	
	@Override
	public boolean includeIfEmpty() {
		return true;
	}

}
