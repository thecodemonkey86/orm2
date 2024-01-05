package cpp.entityhelper;

import java.util.Collection;

import cpp.Namespaces;
import cpp.core.Cls;
import cpp.entity.EntityCls;
import cpp.entityhelper.method.MethodFillFromForm;

public class EntityHelper extends Cls{

	public EntityHelper(Collection<EntityCls> entities) {
		super("EntityHelper");
		for(EntityCls entity:entities) {
			addMethod(new MethodFillFromForm(entity));
			addMethod(new MethodFillFromForm(entity, true));
			addIncludeHeader(entity.getHeaderInclude());
		}
		addIncludeHeader("form/form");
		addIncludeLib("memory");
		setUseNamespace(Namespaces.std);
	}

}
