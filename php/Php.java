package php;

public abstract class Php {
	public static Php phpVersion = new Php5() ;
	
	public static void setPhpVersion(Php phpVersion) {
		Php.phpVersion = phpVersion;
	}
	
	public abstract boolean supportsTypeHints(); 
	public abstract boolean supportsNullableTypeHint(); 
	
}
