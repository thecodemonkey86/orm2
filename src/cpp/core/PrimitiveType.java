package cpp.core;

public class PrimitiveType extends Type{

	boolean unsigned;
	
	public PrimitiveType(String type) {
		this(type,false);
	}
	
	public PrimitiveType(String type,boolean unsigned) {
		super(type);
		this.unsigned=unsigned;
	}

	@Override
	public boolean isPrimitiveType() {
		return true;
	}
	
	@Override
	public boolean isUnsigned() {
		return unsigned;
	}
	
}
