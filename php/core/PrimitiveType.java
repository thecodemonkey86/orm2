package php.core;

public class PrimitiveType extends Type{

	public PrimitiveType(String type) {
		super(type);
	}

	@Override
	public boolean isPrimitiveType() {
		return true;
	}

	@Override
	public Type toNullable() {
		return this;
	}

	
}
