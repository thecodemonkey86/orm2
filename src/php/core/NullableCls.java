package php.core;

import php.core.method.Method;

public class NullableCls extends PhpCls implements INullable{
	public NullableCls(AbstractPhpCls type, String namespace) {
		super(type.getName(), namespace);
		this.type = type;
	}

	AbstractPhpCls type;
	
	@Override
	public String toDeclarationString() {
		return "?"+ type.toDeclarationString();
	}
	
	@Override
	public Attr getAttr(Attr prototype) {
		return ((IAttributeContainer) type).getAttr(prototype);
	}
	
	@Override
	public Attr getAttrByName(String name) {
		return ((IAttributeContainer) type).getAttrByName(name);
	}
	
	@Override
	public Method getMethod(String name) {
		return type.getMethod(name);
	}
	
	@Override
	public Constructor getConstructor() {
		return ((PhpCls) type).getConstructor();
	}
	
	@Override
	public Attr getStaticAttribute(String name) {
		return ((PhpCls) type).getStaticAttribute(name);
	}
}
