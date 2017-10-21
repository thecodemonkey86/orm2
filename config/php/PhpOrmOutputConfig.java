package config.php;

import config.OrmConfig;

public class PhpOrmOutputConfig extends OrmConfig{
	protected String beanPackageName,repositoryPackageName;
	
	public String getBeanPackageName() {
		return beanPackageName;
	}

	public String getRepositoryPackageName() {
		return repositoryPackageName;
	}
	
	public void setBeanPackageName(String beanPackageName) {
		this.beanPackageName = beanPackageName;
	}
	
	public void setRepositoryPackageName(String repositoryPackageName) {
		this.repositoryPackageName = repositoryPackageName;
	}
}
