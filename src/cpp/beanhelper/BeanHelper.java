package cpp.beanhelper;

import java.util.Collection;

import cpp.bean.BeanCls;
import cpp.beanhelper.method.MethodFillFromForm;
import cpp.core.Cls;

public class BeanHelper extends Cls{

	public BeanHelper(Collection<BeanCls> beans) {
		super("BeanHelper");
		for(BeanCls bean:beans) {
			addMethod(new MethodFillFromForm(bean));
			addMethod(new MethodFillFromForm(bean, true));
			addIncludeHeader("model/beans/"+ bean.getIncludeHeader());
		}
		addIncludeHeader("form/form");
		addIncludeLib("memory");
		setUseNamespace("std");
	}

}
