package database;

import java.util.Properties;

public class FirebirdCredentials extends DbCredentials{
	protected String user, host, file, charSet;
	
	protected int port;
	
	public FirebirdCredentials(String user, String host, String file, int port, String charSet, Database db) {
		super(db);
		this.user = user;		
		this.host = host;
		this.port = port;
		this.file = file;
		this.charSet = charSet;
	}

	
	public String getUser() {
		return user;
	}

	@Override
	public Properties getProperties() {
		Properties props = new Properties();
		// props.setProperty("password", "postgres");
		props.setProperty("charSet", charSet);
		props.setProperty("user", user);
		props.setProperty("password", getPassword());
		return props;
	}

	@Override
	public String getConnectionUrl() {
		return "jdbc:firebirdsql:" + host+ "/"+port +":"+file;
	}
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getFile() {
		return file;
	}
}
