package sunjava.core;

import codegen.CodeUtil;

public class JavaGenericClass extends JavaCls {
	
	protected Type element;
	
	public JavaGenericClass(String type, Type element, String pkg) {
		super(type,pkg);
		this.element = element.isPrimitiveType() ? ((PrimitiveType) element).getAutoBoxingClass() : element;
	}
	
//	@Override
//	public String getQualifiedName() {
//		return type + CodeUtil.abr(element.getQualifiedName());
//	}
	
	
	
	@Override
	public String getConstructorName() {
		return type ;
	}
	
	@Override
	public String toDeclarationString() {
		return type + CodeUtil.abr(element.toString());
	}
	

	public Type getElementType() {
		return element;
	}
	
	@Override
	public void collectImports(AbstractJavaCls cls) {
		element.collectImports(cls);
		super.collectImports(cls);
	}
}
