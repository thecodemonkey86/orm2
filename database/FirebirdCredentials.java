package database;

import java.util.Properties;

public class FirebirdCredentials extends DbCredentials{
	protected String user, password, host, file, charSet;
	
	protected int port;
	
	public FirebirdCredentials(String user, String password, String host, String file, int port, String charSet, Database db) {
		super(db);
		this.user = user;
		this.password = password;
		this.host = host;
		this.port = port;
		this.file = file;
		this.charSet = charSet;
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
		props.setProperty("charSet", charSet);
		props.setProperty("user", user);
		props.setProperty("password", password);
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
