package database;

import java.nio.file.Path;
import java.util.Properties;

public class SqliteCredentials extends DbCredentials {

	protected Path file;
	
	
	public SqliteCredentials(Path file,  Database db) {
		super(db);
		this.file = file;
	
	}
	
	@Override
	public Properties getProperties() {
		Properties props = new Properties();
		String password = getPassword();
		if(password != null && !password.isEmpty())
			props.setProperty("password", password);
		//props.setProperty("journal_mode", "WAL");
		return props;
	}

	@Override
	public String getConnectionUrl() {
		return String.format("jdbc:sqlite:%s", file.toString().replace('\\', '/'));
	}

}
