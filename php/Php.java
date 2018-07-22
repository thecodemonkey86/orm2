package php;

public abstract class Php {
	public static Php phpVersion = new Php7_2() ;
	
	public static void setPhpVersion(Php phpVersion) {
		Php.phpVersion = phpVersion;
	}
	
	public abstract boolean supportsTypeHints(); 
	public abstract boolean supportsNullableTypeHint(); 
	
}
