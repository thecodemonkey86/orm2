package database;

import java.util.Properties;

public class PgCredentials extends DbCredentials{
	protected String user, host;
	protected int port;
	
	public PgCredentials(String user, String host,int port, Database db) {
		super(db);
		this.user = user;		
		if(user==null) {
			throw new RuntimeException("user is missing");
		}
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
		return "jdbc:postgresql://" + host +   ":"+ port+ "/" + db.getName();
	}
}
