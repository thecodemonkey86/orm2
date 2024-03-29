package cpp.core;

import codegen.CodeUtil;

public class RValueRef extends Cls {

	Type base;
	
	public RValueRef(Type type) {
		super(null);
		
		this.base = type;
	}
	@Override
	public String toUsageString() {
		return CodeUtil.sp(base.toUsageString(),"&&" );
	}
	
	
	@Override
	public String getName() {
		return base.getName();
	}

	@Override
	public String toDeclarationString() {
		
		return CodeUtil.sp(base.toUsageString(),"&&" );
	}
	
	@Override
	public Attr getAttrByName(String name) {
		if (base instanceof IAttributeContainer) {
			IAttributeContainer c=(IAttributeContainer) base;
			return c.getAttrByName(name);
		} 
		throw new RuntimeException("type is not a class or a struct: "+name);
	}
	
	@Override
	public Attr getAttr(Attr prototype) {
		if (base instanceof IAttributeContainer) {
			IAttributeContainer c=(IAttributeContainer) base;
			return c.getAttr(prototype);
		} 
		throw new RuntimeException("type is not a class or a struct: "+type);
	}
	
	@Override
	public Method getMethod(String name) {
		if (!(base instanceof Cls)) {
			throw new RuntimeException("type is not a class "+base);
		}
		return ((Cls) base).getMethod(name);
	}
	
	public Type getBase() {
		return base;
	}
	
	@Override
	public boolean isPtr() {
		return base.isPtr();
	}
}
