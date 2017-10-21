package php.core;

import java.util.ArrayList;

import php.core.method.Method;

public abstract class AbstractPhpCls extends Type {

	protected ArrayList<Method> methods;
	protected String namespace;
	
	public AbstractPhpCls(String name,String namespace) {
		super(name);
		this.methods=new ArrayList<>();
		
		if(namespace != null && namespace.contains(".")) {
			throw new RuntimeException("invalid namespace: " + namespace);
		} 
		if(namespace != null && !namespace.startsWith("\\")) {
			this.namespace = "\\" + namespace;
		}  else {
			this.namespace=namespace;
		}
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public void addMethod(Method m) {
		for(Method m0:methods) {
			if(m0.getName().equals(m.getName())) {
				throw new RuntimeException("duplicate method: " +m.getName());
			}
		}
		
		methods.add(m);
		m.setParent(this);
	}


	public Method getMethod(String name) {
		for(Method m:methods) {
			if (m.getName().equals(name)) {
				return m;
			}
		}
		throw new RuntimeException("no such method "+getClass().getName()+"|"+ getName()+"::"+name);
	}

	public String getFullyQualifiedName() {
		return namespace + "\\" +getName();
	}


}
