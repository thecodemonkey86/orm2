package cpp.core;

import codegen.CodeUtil;

public class ConstRef extends Cls {

	Type base;
	
	public ConstRef(Type type) {
		super(null);
		this.base = type;
		this.constness = true;
	}
	@Override
	public String toUsageString() {
		return CodeUtil.sp(constness&& !base.constness ?"const":null,base.toUsageString(),"&" );
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ConstRef) {
			ConstRef cr = (ConstRef) obj;
			return base.equals(cr.base);
		} else {
			return false;
		}
	} 
	
	
	@Override
	public String getName() {
		return base.getName();
	}

	@Override
	public String toDeclarationString() {
		
		return CodeUtil.sp(constness && !base.constness ?"const":null,base.toUsageString(),"&" );
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
	public Attr getAttrByName(String name) {
		if (base instanceof IAttributeContainer) {
			IAttributeContainer c=(IAttributeContainer) base;
			return c.getAttrByName(name);
		} 
		throw new RuntimeException("type is not a class or a struct: "+name);
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
