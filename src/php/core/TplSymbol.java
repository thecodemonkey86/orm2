package php.core;

public class TplSymbol extends Type{

	public TplSymbol(String type) {
		super(type);
	}

	@Override
	public NullableType toNullable() {
		return null;
	}

}
