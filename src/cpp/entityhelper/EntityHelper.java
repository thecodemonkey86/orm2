package cpp.entityhelper;

import java.util.Collection;

import cpp.core.Cls;
import cpp.entity.EntityCls;
import cpp.entityhelper.method.MethodFillFromForm;

public class EntityHelper extends Cls{

	public EntityHelper(Collection<EntityCls> beans) {
		super("EntityHelper");
		for(EntityCls bean:beans) {
			addMethod(new MethodFillFromForm(bean));
			addMethod(new MethodFillFromForm(bean, true));
			addIncludeHeader("model/beans/"+ bean.getIncludeHeader());
		}
		addIncludeHeader("form/form");
		addIncludeLib("memory");
		setUseNamespace("std");
	}

}
