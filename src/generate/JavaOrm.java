package generate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import config.OrmConfig;
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
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.table.Table;
import io.PasswordManager;
import sunjava.config.JavaConfigReader;
import sunjava.config.JavaOrmOutputConfig;
import sunjava.core.JavaCls;
import sunjava.core.Types;
import sunjava.entity.CustomClassMemberCode;
import sunjava.entity.Entities;
import sunjava.entity.EntityCls;
import sunjava.entityrepository.ClsEntityRepository;
import sunjava.entityrepository.query.ClsEntityQuery;
import sunjava.orm.PgDatabaseMapper;
import xml.reader.DefaultXMLReader;

public class JavaOrm extends OrmGenerator{
	private static final String BEGIN_CUSTOM_CLASS_MEMBERS = "/*BEGIN_CUSTOM_CLASS_MEMBERS*/";
	private static final String END_CUSTOM_CLASS_MEMBERS = "/*END_CUSTOM_CLASS_MEMBERS*/";
	
	private static final StandardOpenOption[] writeOptions={
		StandardOpenOption.CREATE,StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING
			
	};

	public JavaOrm(OrmConfig cfg) {
		super(cfg);
	}
	
	public void generate()  {
		try {
			JavaOrmOutputConfig cfg = (JavaOrmOutputConfig) this.cfg;
			Charset utf8 = Charset.forName("UTF-8");
			Class.forName("org.postgresql.Driver");

			ClsEntityQuery.setEntityQueryPackage(cfg.getRepositoryPackageName()+".query");
			EntityCls.setEntityClsPackage(cfg.getEntityPackageName());
			ClsEntityRepository.setEntityRepositoryPackage(cfg.getRepositoryPackageName());
			
			EntityCls.setDatabase( cfg.getDatabase());
			EntityCls.setTypeMapper(new PgDatabaseMapper());
			EntityCls.setSqlQueryCls(Types.PgSqlQuery);
			Path path = cfg.getBasePath();
			Path modelPath = cfg.getModelPath();
			Path repositoryPath = cfg.getRepositoryPath();
			Path entitiesPath = modelPath.resolve("entities");
			Path fetchListHelperPath = entitiesPath.resolve("helper");
			
			
			ClsEntityRepository repo=Types.EntityRepository;
			
			for(Table tbl:cfg.getEntityTables()  ) {
				
				List<OneToManyRelation> manyRelations = cfg.getOneToManyRelations(tbl);
				List<OneRelation> oneRelations = cfg.getOneRelations(tbl);
				List<ManyRelation> manyToManyRelations = cfg.getManyRelations(tbl);
//				tableOneRelations.put(tbl, oneRelations);
//				tableManyRelations.put(tbl, manyRelations);
				
				
				EntityCls cls = new EntityCls(tbl,manyRelations, oneRelations,manyToManyRelations);
				Entities.add(cls);
			}
			
			
			for (EntityCls c : Entities.getAllEntities()) {
				c.addDeclarations();
			}
			
			repo.addDeclarations(Entities.getAllEntities());
			for (EntityCls c : Entities.getAllEntities()) {
				//Path pathHeader = Paths.get(path+"/entities/"+c.getName().toLowerCase()+".h");
				Path pathSrc = Paths.get(path+"/entities/"+c.getName()+".java");
				
				
				if (Files.exists(pathSrc)) {
				
					String existingSourceFile = new String(Files.readAllBytes(pathSrc),utf8);
					
					
					StringBuilder sbSrc = new StringBuilder();
					 
					int startSrc = existingSourceFile.indexOf(BEGIN_CUSTOM_CLASS_MEMBERS);
					int endSrc = existingSourceFile.indexOf(END_CUSTOM_CLASS_MEMBERS,startSrc+BEGIN_CUSTOM_CLASS_MEMBERS.length());
					if (startSrc > -1 && endSrc > -1) {
						//String customClassMember = existingHeaderFile.substring(startHdr, endHdr+BEGIN_CUSTOM_CLASS_MEMBERS.length()-1);
						String implCode = existingSourceFile.substring(startSrc, endSrc+BEGIN_CUSTOM_CLASS_MEMBERS.length()-1);
						c.addMethod(new CustomClassMemberCode( implCode) );
						
						sbSrc.append(implCode).append('\n');
					}
						 
					if (sbSrc.length()>0)
						Files.write(Paths.get("bak_custom_class_members", pathSrc.getFileName().toString()),sbSrc.toString().getBytes(utf8), writeOptions);
					
				}
				
				c.addMethodImplementations();
				if(c.getTbl().getPrimaryKey().isMultiColumn())
					((JavaCls) c.getPkType()).addMethodImplementations();
			}
			repo.addMethodImplementations();
//			List<ManyRelation> list = tableManyRelations.get(getTableByName("artist"));
//			System.out.println(list);
			
			Files.createDirectories(fetchListHelperPath);
			Files.createDirectories(repositoryPath.resolve("query"));
			
			for (EntityCls c : Entities.getAllEntities()) {
				Path pathSrc = entitiesPath.resolve(c.getName()+".java");
				Path pathHelperSrc = fetchListHelperPath.resolve(c.getFetchListHelperCls().getName()+".java");
						
				Files.write(pathSrc, c.toSourceString().getBytes(utf8), writeOptions);
				Files.write(pathHelperSrc, c.getFetchListHelperCls().toSourceString().getBytes(utf8), writeOptions);
				ClsEntityQuery clsQuery = new ClsEntityQuery(c);
				clsQuery.addMethodImplementations();
				Files.write(repositoryPath.resolve("query").resolve(clsQuery.getName()+".java"), clsQuery.toSourceString().getBytes(utf8), writeOptions);
				
				if(c.getTbl().getPrimaryKey().isMultiColumn()) {
					Path pathMultiColumnPkType = entitiesPath.resolve("pk").resolve(c.getPkType().getName()+".java");
					Files.createDirectories(entitiesPath.resolve("pk"));
					Files.write(pathMultiColumnPkType, ((JavaCls) c.getPkType()).toSourceString().getBytes(utf8), writeOptions);
				}
			}
			
			Files.write(repositoryPath.resolve("EntityRepository.java"), repo.toSourceString().getBytes(utf8), writeOptions);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
		

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
			} else if(args[i].equals("--host")) {
				dbPort = args[i+1];
			} else if(args[i].equals("--dbFile")) {
				dbFile = args[i+1];	
			} else if(args[i].equals("--charset")) {
				charset = args[i+1];
			}  else if(args[i].equals("--user")) {
				dbUser = args[i+1];
			}
		}
		
		final Database database; 
		DbCredentials credentials;
		
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
			String password = !setPass ? PasswordManager.loadFromFile(credentials) : null;
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
			
			
			
			Properties props = credentials.getProperties();
			props.setProperty("charSet",charset);
			
			// props.setProperty("user", "postgres");
			
			Connection conn = DriverManager.getConnection(credentials.getConnectionUrl(), credentials.getProperties());
		
		
			
			JavaConfigReader cfgReader = new JavaConfigReader(xmlFile.getParent(),conn,database);
			DefaultXMLReader.read(xmlFile, cfgReader);
			JavaOrmOutputConfig cfg = cfgReader.getCfg();
			
			
			cfg.setDbEngine(engine);
			
			new JavaOrm(cfg).generate();
		} finally {
			
		}
	}

}
