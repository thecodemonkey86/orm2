package php.cls;

import php.IAttributeContainer;

public class Ref extends Type {

	Type base;
	
	public Ref(Type type) {
		super(null);
		
		this.base = type;
	}
		
	
	@Override
	public String getName() {
		return base.getName();
	}

	@Override
	public String toDeclarationString() {
		return base.toDeclarationString()+"&";
	}
	
	public Attr getAttrByName(String name) {
		if (base instanceof IAttributeContainer) {
			IAttributeContainer c=(IAttributeContainer) base;
			return c.getAttrByName(name);
		} 
		throw new RuntimeException("type is not a class or a struct: "+base);
	}
	
	public Attr getAttr(Attr prototype) {
		if (base instanceof IAttributeContainer) {
			IAttributeContainer c=(IAttributeContainer) base;
			return c.getAttr(prototype);
		} 
		throw new RuntimeException("type is not a class or a struct: "+base);
	}
	
	public Method getMethod(String name) {
		if (!(base instanceof PhpCls)) {
			throw new RuntimeException("type is not a class "+base);
		}
		return ((PhpCls) base).getMethod(name);
	}
	
	public Type getBase() {
		return base;
	}
}

