package php.core;

public class NullableType extends Type{
	public NullableType(Type type) {
		super(type.getName());
		this.type = type;
	}

	Type type;
	
	@Override
	public String toDeclarationString() {
		return "?"+ type.toDeclarationString();
	}
}
