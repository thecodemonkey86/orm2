package sunjava.cls.bean.repo.query.method;

import sunjava.Types;
import sunjava.cls.Method;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.bean.repo.ClsBeanRepository;
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
