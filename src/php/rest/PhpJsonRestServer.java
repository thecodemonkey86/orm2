package php.rest;

import java.util.Collection;

import php.bean.BeanCls;
import php.core.Constructor;
import php.core.PhpCls;
import php.rest.method.RestMethodGet;
import php.rest.method.RestMethodGetById;
import php.rest.method.RestMethodGetOne;

public class PhpJsonRestServer extends PhpCls {
	
	public PhpJsonRestServer(Collection<BeanCls> beans) {
		super("PhpJsonRestServer", null);
		addMethod(new RestMethodGet(beans));
		addMethod(new RestMethodGetOne(beans));
		addMethod(new RestMethodGetById(beans));
		setConstructor(new Constructor() {
			
			@Override
			public void addImplementation() {
			}
		});
	}
}
