package php.rest;

import java.util.Collection;

import php.core.Constructor;
import php.core.PhpCls;
import php.entity.EntityCls;
import php.rest.method.RestMethodGetList;
import php.rest.method.RestMethodDelete;
import php.rest.method.RestMethodGetById;
import php.rest.method.RestMethodGetOne;
import php.rest.method.RestMethodSave;

public class PhpJsonRestServer extends PhpCls {
	
	public PhpJsonRestServer(Collection<EntityCls> entities) {
		super("PhpJsonRestServer", null);
		addMethod(new RestMethodGetList(entities));
		addMethod(new RestMethodGetOne(entities));
		addMethod(new RestMethodGetById(entities));
		addMethod(new RestMethodSave(entities));
		addMethod(new RestMethodDelete(entities));
		setConstructor(new Constructor() {
			
			@Override
			public void addImplementation() {
			}
		});
	}
}
