package cpp.entityhelper;

import java.util.Collection;

import cpp.Namespaces;
import cpp.core.Cls;
import cpp.entity.EntityCls;
import cpp.entityhelper.method.MethodFillFromForm;

public class EntityHelper extends Cls{

	public EntityHelper(Collection<EntityCls> beans) {
		super("EntityHelper");
		for(EntityCls bean:beans) {
			addMethod(new MethodFillFromForm(bean));
			addMethod(new MethodFillFromForm(bean, true));
			addIncludeHeader(bean.getHeaderInclude());
		}
		addIncludeHeader("form/form");
		addIncludeLib("memory");
		setUseNamespace(Namespaces.std);
	}

}
