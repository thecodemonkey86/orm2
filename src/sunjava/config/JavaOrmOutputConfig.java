package sunjava.config;

import config.OrmConfig;

public class JavaOrmOutputConfig extends OrmConfig{
	protected String entityPackageName,repositoryPackageName;
	
	public String getEntityPackageName() {
		return entityPackageName;
	}

	public String getRepositoryPackageName() {
		return repositoryPackageName;
	}
	
	public void setEntityPackageName(String beanPackageName) {
		this.entityPackageName = beanPackageName;
	}
	
	public void setRepositoryPackageName(String repositoryPackageName) {
		this.repositoryPackageName = repositoryPackageName;
	}
}
