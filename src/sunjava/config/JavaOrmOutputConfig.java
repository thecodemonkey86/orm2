package sunjava.config;

import config.OrmConfig;

public class JavaOrmOutputConfig extends OrmConfig{
	protected String entityPackageName,repositoryPackageName;
	protected static boolean android;
	
	public String getEntityPackageName() {
		return entityPackageName;
	}

	public String getRepositoryPackageName() {
		return repositoryPackageName;
	}
	
	public void setEntityPackageName(String entityPackageName) {
		this.entityPackageName = entityPackageName;
	}
	
	public void setRepositoryPackageName(String repositoryPackageName) {
		this.repositoryPackageName = repositoryPackageName;
	}
	
	public static void setAndroid(boolean android) {
		JavaOrmOutputConfig.android = android;
	}
	public static boolean isAndroid() {
		return android;
	}
}
