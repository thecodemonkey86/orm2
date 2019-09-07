package config.php;

import config.OrmConfig;
import php.Php;
import php.Php5;

public class PhpOrmConfig extends OrmConfig{
	protected String namespace;
	protected Php phpversion;
	
	public void setPhpversion(Php phpversion) {
		this.phpversion = phpversion;
	}
	
	public Php getPhpversion() {
		if(phpversion == null) {
			phpversion = new Php5();
		}
		return phpversion;
	}
}
