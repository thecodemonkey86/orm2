package cpp.core;

public class TplSymbol extends Type{

	public TplSymbol(String type) {
		super(type);
	}
	
	@Override
	public String toDeclarationString() {
		return getName();
	}

	@Override
	public String toUsageString() {
		return getName();
	}

	public SharedPtr toSharedPtr() {
		return new SharedPtr(this);
	}
}
