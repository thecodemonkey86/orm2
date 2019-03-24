package php.core;

import java.util.ArrayList;
import java.util.List;

import php.core.method.Method;
import util.Pair;

public abstract class AbstractPhpCls extends Type {

	protected ArrayList<Method> methods;
	protected String namespace;
	protected List<Pair<String,String>> renameMethods;
	
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
				if(renameMethods != null) {
					boolean renamed = false;
					for(Pair<String,String> r : renameMethods) {
						if(r.getValue1().equals(m0.getName())) {
							m0.setName(r.getValue2());
							renamed = true;
							break;
						}
					}
					if(renamed) {
						break;
					}
				}
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

	@Override
	public NullableCls toNullable() {
		return new NullableCls(this, namespace);
	}
	
	public void setRenameMethods(List<Pair<String, String>> renameMethods) {
		this.renameMethods = renameMethods;
	}
}
