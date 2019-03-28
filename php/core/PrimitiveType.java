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
	public NullableType toNullable() {
		return new NullableType(this);
	}

	
}
