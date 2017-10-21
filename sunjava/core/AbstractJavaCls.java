package sunjava.core;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public abstract class AbstractJavaCls extends Type {

	protected ArrayList<Method> methods;
	protected String pkg;
	protected LinkedHashSet<String> imports;
	
	public AbstractJavaCls(String name,String pkg) {
		super(name);
		this.methods=new ArrayList<>();
		this.imports=new LinkedHashSet<>();
		this.pkg=pkg;
	}
	
	public String getPackage() {
		return pkg;
	}
	
	public void addMethod(Method m) {
		methods.add(m);
		m.setParent(this);
	}

	public void addImport(JavaCls cls) {
		addImport(cls.getFullyQualifiedName());
	}

	public void addImport(String i) {
		if (!i.equals(getFullyQualifiedName()))
			imports.add(i);
	}
	

//	@Override
//	public boolean hasImport() {
//		return pkg != null && !pkg.startsWith("java.lang");
//	}
//	
//	@Override
//	public String getImport() {
//		if (pkg == null) {
//			throw new RuntimeException("package declaration missing for class "+getName());
//		}
//		return pkg + "." +getName();
//	}
	
	public Method getMethod(String name) {
		for(Method m:methods) {
			if (m.getName().equals(name)) {
				return m;
			}
		}
		throw new RuntimeException("no such method "+getClass().getName()+"|"+ getName()+"::"+name);
	}

//	public void addImport(Type type) {
//		if(type.hasImport()) {
//			addImport(type.getImport());
//		}
//		
//	}
	
	@Override
	public void collectImports(AbstractJavaCls cls) {
		if(pkg != null && !pkg.startsWith("java.lang")) {
			cls.addImport(pkg + "." +getName());
		}
	}
	
	public String getFullyQualifiedName() {
		return pkg + "." +getName();
	}


}
