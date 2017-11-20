package database;

import java.util.Properties;

public abstract class DbCredentials {
	protected Database db;
	
	public DbCredentials(Database db) {
		this.db = db;
	}
	
	public abstract Properties getProperties();
	public abstract String getConnectionUrl();
}
