package php.core;

public class PhpArray extends PhpPseudoGenericClass {

	private Type keyType;
	
	public PhpArray(Type valueType) {
		this(Types.Int, valueType);
	}
	
	public PhpArray(Type keyType,Type valueType) {
		super("array", valueType, null);
		this.keyType = keyType;
	}
	
	public Type getKeyType() {
		return keyType;
	}
	
	@Override
	public String toDeclarationString() {
		return "array";
	}

}
