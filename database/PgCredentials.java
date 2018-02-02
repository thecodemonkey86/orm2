package database;

import java.util.Properties;

public class PgCredentials extends DbCredentials{
	protected String user, password, host;
	protected int port;
	
	public PgCredentials(String user, String password, String host,int port, Database db) {
		super(db);
		this.user = user;
		this.password = password;
		this.host = host;		
		this.port = port;
	}

	public String getPassword() {
		return password;
	}
	
	public String getUser() {
		return user;
	}

	@Override
	public Properties getProperties() {
		Properties props = new Properties();
		// props.setProperty("password", "postgres");
		props.setProperty("user", user);
		props.setProperty("password", password);
		return props;
	}

	@Override
	public String getConnectionUrl() {
		return "jdbc:postgresql://" + host +   ":"+ port+ "/" + db.getName();
	}
}
