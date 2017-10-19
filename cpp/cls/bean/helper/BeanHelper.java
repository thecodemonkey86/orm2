package cpp.cls.bean.helper;

import java.util.Collection;

import cpp.cls.Cls;
import cpp.cls.bean.BeanCls;
import cpp.cls.bean.helper.method.MethodFillFromForm;

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
