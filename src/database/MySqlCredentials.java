package database;

import java.util.Properties;

public class MySqlCredentials extends DbCredentials{
	protected String user, host;
	protected int port;
	
	public MySqlCredentials(String user, String host,int port, Database db) {
		super(db);
		this.user = user;
		this.host = host;	
		this.port = port;
	}


	public String getUser() {
		return user;
	}

	@Override
	public Properties getProperties() {
		Properties props = new Properties();
		// props.setProperty("password", "postgres");
		props.setProperty("user", user);
		props.setProperty("password", getPassword());
		return props;
	}

	@Override
	public String getConnectionUrl() {
		return String.format("jdbc:mysql://%s:%d/%s?useSSL=false", host ,port, db.getName());
	}
}
