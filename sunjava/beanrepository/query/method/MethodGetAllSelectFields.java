package sunjava.beanrepository.query.method;

import sunjava.bean.BeanCls;
import sunjava.beanrepository.ClsBeanRepository;
import sunjava.core.Method;
import sunjava.core.Types;
import sunjava.lib.ClsBaseBeanQuery;


public class MethodGetAllSelectFields extends Method {
	
	protected BeanCls bean;
	
	public MethodGetAllSelectFields(BeanCls cls) {
		super(Public, Types.String, "getAllSelectFields" );
		this.bean = cls;
		setOverrideAnnotation(true);
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
