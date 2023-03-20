package php.rest;

import java.util.Collection;

import php.core.Constructor;
import php.core.PhpCls;
import php.entity.EntityCls;
import php.rest.method.RestMethodGet;
import php.rest.method.RestMethodGetById;
import php.rest.method.RestMethodGetOne;
import php.rest.method.RestMethodSave;

public class PhpJsonRestServer extends PhpCls {
	
	public PhpJsonRestServer(Collection<EntityCls> beans) {
		super("PhpJsonRestServer", null);
		addMethod(new RestMethodGet(beans));
		addMethod(new RestMethodGetOne(beans));
		addMethod(new RestMethodGetById(beans));
		addMethod(new RestMethodSave(beans));
		setConstructor(new Constructor() {
			
			@Override
			public void addImplementation() {
			}
		});
	}
}
