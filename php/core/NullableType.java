package php.core;

import php.core.method.Method;

public class NullableType extends PhpPseudoGenericClass {

	public NullableType(Type type) {
		super(type.getName(), type, null);
	}
	
	public NullableType(Type type, String namespace) {
		super(type.getName(), type, namespace);
	}
	@Override
	public Attr getAttrByName(String name) {
		if (!(element instanceof PhpCls)) {
			throw new RuntimeException("type "+type+" is not a class"); 
		}
		return ((PhpCls) element).getAttrByName(name);
	}
	
	@Override
	public Attr getAttr(Attr prototype) {
		if (element instanceof IAttributeContainer) {
			IAttributeContainer c=(IAttributeContainer) element;
			return c.getAttr(prototype);
		} 
		throw new RuntimeException("type is not a class or a struct: "+getName());
	}
	
	@Override
	public Method getMethod(String name) {
		if (!(element instanceof PhpCls)) {
			throw new RuntimeException("type "+type+" is not a class"); 
		}
		return ((PhpCls) element).getMethod(name);
	}
	
	@Override
	public String toDeclarationString() {
		return element.toDeclarationString();
	}
	@Override
	public String toNullableDeclarationString() {
		return "?"+ element.toDeclarationString();
	}
	
	

}
