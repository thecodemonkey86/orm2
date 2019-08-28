package php;

public abstract class Php {
	public static Php phpVersion;
	
	
	
	public abstract boolean supportsTypeHints(); 
	public abstract boolean supportsNullableTypeHint(); 
	
}
