package sunjava.cls;

public abstract class PrimitiveType extends Type{

	public PrimitiveType(String type) {
		super(type);
	}

	@Override
	public boolean isPrimitiveType() {
		return true;
	}

	public abstract JavaCls getAutoBoxingClass();
}
