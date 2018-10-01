package database;

import java.util.Properties;

public abstract class DbCredentials {
	protected Database db;
	private String password;
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	public DbCredentials(Database db) {
		this.db = db;
	}
	
	public abstract Properties getProperties();
	public abstract String getConnectionUrl();
}
