package sunjava.config;

import config.OrmConfig;

public class JavaOrmOutputConfig extends OrmConfig{
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
