package generate;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import config.json.JsonModeConfigReader;
import database.Database;
import database.DbCredentials;
import database.FirebirdCredentials;
import database.FirebirdDatabase;
import database.MySqlCredentials;
import database.MySqlDatabase;
import database.PgCredentials;
import database.PgDatabase;
import database.SqliteCredentials;
import database.SqliteDatabase;
import io.PasswordManager;
import util.Pair;

public class JsonOrmGenerator {

	public static void main(String[] args) throws Exception {
		if(args.length == 0) {
			throw new Exception("Please provide xml config file");
		}
		PasswordManager.setSuperPassword(new byte[] {
				0x7,
				58,
				1,
				0xf,
				0x7f,
				0x8,
				65,
				0x58
		});
		
		
		Path xmlFile = Paths.get(args[args.length-1]);
		
		boolean setPass= args[0].equals("--setpass");
		
		String engine = null;
		String dbName = null;
		String dbSchema = null;
		String dbUser = null;
		String dbPort = null;
		String dbHost = null;
		String dbFile = null;
		String charset = "utf8" ;
		
		for(int i=0;i<args.length-1;i++) {
			if(args[i].equals("--engine")) {
				engine = args[i+1];
			} else if(args[i].equals("--name")) {
				dbName = args[i+1];
			} else if(args[i].equals("--schema")) {
				dbSchema = args[i+1];
			} else if(args[i].equals("--host")) {
				dbHost = args[i+1];
			} else if(args[i].equals("--port")) {
				dbPort = args[i+1];
			} else if(args[i].equals("--user")) {
				dbUser = args[i+1];
			} else if(args[i].equals("--dbFile")) {
				dbFile = args[i+1];	
			} else if(args[i].equals("--charset")) {
				charset = args[i+1];
			}
		}
		
		final Database database; 
		DbCredentials credentials;
		
		if(engine==null) {
			System.out.println("engine param missing");
			System.exit(1);
			return;
		}
		
		if (engine.equals("postgres")) {
			Class.forName("org.postgresql.Driver");
			database = new PgDatabase(dbName, dbSchema);
			credentials = new PgCredentials(dbUser, dbHost,dbPort != null ? Integer.parseInt(dbPort) : 5432, database);
			
		} else if (engine.equals("mysql")) {
			database = new MySqlDatabase(dbName);
			credentials = new MySqlCredentials(dbUser, dbHost, dbPort != null ? Integer.parseInt(dbPort) : 3306, database);
			
		} else if (engine.equals("firebird")) {
			Class.forName("org.firebirdsql.jdbc.FBDriver");
			database = new FirebirdDatabase(dbName);
			credentials = new FirebirdCredentials(dbUser, dbHost, dbFile,dbPort != null ? Integer.parseInt(dbPort) : 23053, charset  != null ?  charset  : "UTF-8", database);
		} else if (engine.equals("sqlite")) {
			Class.forName("org.sqlite.JDBC");
			database = new SqliteDatabase();
			credentials = new SqliteCredentials(Paths.get(dbFile) , database);
				
		} else {
			throw new IOException(
					"Database engine \"" + engine + "\" is currently not supported");
		}
		try(database) {
			String password =!setPass ? PasswordManager.loadFromFile(credentials) : null; 
			if(password == null && !engine.equals("sqlite")) {
				JPasswordField jpf = new JPasswordField(24);
			    JLabel jl = new JLabel("Passwort: ");
			    Box box = Box.createHorizontalBox();
			    box.add(jl);
			    box.add(jpf);
			    int x = JOptionPane.showConfirmDialog(null, box, "DB Passwort", JOptionPane.OK_CANCEL_OPTION);
	
			    if (x == JOptionPane.OK_OPTION) {
			    	password = new String(jpf.getPassword());
			    }
				
				if(password != null && !password.isEmpty()) {
					PasswordManager.saveToFile(credentials, password);
				} else {
					throw new IOException("Password not set");
				}
			}
			credentials.setPassword(password);
			if(setPass) {
				PasswordManager.saveToFile(credentials, password);
			}
			credentials.setPassword(password);
			
			Properties props = credentials.getProperties();
			props.setProperty("charSet",charset);
			
			// props.setProperty("user", "postgres");
			
			Connection conn = DriverManager.getConnection(credentials.getConnectionUrl(), credentials.getProperties());
			Pair<OrmGenerator, OrmGenerator> ormGenerators = JsonModeConfigReader.read(xmlFile,conn,engine,database);
			ormGenerators.getValue1().generate();
			ormGenerators.getValue2().generate();
		} finally {
			
		}
	}

}
