package php.cls.bean.repo.query.method;

import php.Types;
import php.cls.Method;
import php.cls.bean.BeanCls;
import php.cls.bean.repo.ClsBeanRepository;
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
