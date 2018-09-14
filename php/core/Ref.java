package php.core;

import php.core.expression.IArrayAccessible;
import php.core.method.Method;

public class Ref extends Type implements IArrayAccessible{

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
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Ref)
			return base.equals(((Ref)obj).base);
		return false;
	}

	@Override
	public int hashCode() {
		return base.hashCode();
	}

	@Override
	public Type toNullable() {
		if (base instanceof AbstractPhpCls) {
			return new NullableCls((AbstractPhpCls) base, ((AbstractPhpCls) base).getNamespace());
		}
		return new NullableType(base);
	}


	@Override
	public Type getAccessType() {
		if(base instanceof IArrayAccessible) {
			return ((IArrayAccessible)base).getAccessType();
		}
		throw new RuntimeException("type is not ArrayAccessible: "+base);
	}
}

