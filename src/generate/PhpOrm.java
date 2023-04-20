package generate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
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
import config.php.PhpConfigReader;
import config.php.PhpOrmConfig;
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
import php.Php;
import php.core.PhpCls;
import php.core.Types;
import php.core.instruction.Instruction;
import php.entity.CustomClassMemberCode;
import php.entity.Entities;
import php.entity.EntityCls;
import php.entityrepository.ClsEntityRepository;
import php.entityrepository.query.ClsEntityQuery;
import php.orm.DatabaseTypeMapper;
import php.orm.FirebirdDatabaseTypeMapper;
import php.orm.MySqlDatabaseTypeMapper;
import php.orm.PgDatabaseTypeMapper;
import php.rest.PhpJsonRestServer;
import util.StringUtil;
import xml.reader.DefaultXMLReader;

public class PhpOrm extends OrmGenerator {
	private static final String BEGIN_CUSTOM_CLASS_MEMBERS = "/*BEGIN_CUSTOM_CLASS_MEMBERS*/";
	private static final String END_CUSTOM_CLASS_MEMBERS = "/*END_CUSTOM_CLASS_MEMBERS*/";

	private static final StandardOpenOption[] writeOptions = { StandardOpenOption.WRITE, StandardOpenOption.CREATE,
			StandardOpenOption.TRUNCATE_EXISTING

	};

	private DatabaseTypeMapper getTypeMapper(OrmConfig cfg) {
		if (cfg.isEnginePostgres()) {
			return new PgDatabaseTypeMapper();
		} else if (cfg.isEngineMysql()) {
			return new MySqlDatabaseTypeMapper();
		} else if (cfg.isEngineFirebird()) {
			return new FirebirdDatabaseTypeMapper();
		} else {
			throw new RuntimeException("database not yet supported");
		}
	}

	private PhpCls getSqlQueryCls(OrmConfig cfg) {
		if (cfg.isEnginePostgres()) {
			return Types.PgSqlQuery;
		} else if (cfg.isEngineMysql()) {
			return Types.MysqlSqlQuery;
		} else if (cfg.isEngineFirebird()) {
			return Types.FirebirdSqlQuery;
		} else {
			throw new RuntimeException("database not yet supported");
		}
	}

	public PhpOrm(OrmConfig cfg) {
		super(cfg);
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
		
		Database database=null; 
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
	
	
		
		PhpConfigReader cfgReader = new PhpConfigReader(xmlFile.getParent(),conn,database);
		DefaultXMLReader.read(xmlFile, cfgReader);
		PhpOrmConfig cfg = cfgReader.getCfg();
		
		
		cfg.setDbEngine(engine);

		new PhpOrm(cfg).generate(); 
		
	}

	@Override
	public void generate() throws IOException  {
		Php.phpVersion = ((PhpOrmConfig) cfg).getPhpversion();
		EntityCls.setTypeMapper(getTypeMapper(cfg));
		Charset utf8 = Charset.forName("UTF-8");
		ClsEntityRepository.setBeanRepositoryNamespace(cfg.getBasePath().relativize(cfg.getRepositoryPath()).toString().replace("/", "\\"));
		EntityCls.setBeanNamespace(cfg.getBasePath().relativize(cfg.getModelPath()).toString().replace("/", "\\")+"\\Entities");
		EntityCls.setBeanRepoNamespace(cfg.getBasePath().relativize(cfg.getRepositoryPath()).toString().replace("/", "\\"));
		ClsEntityQuery.setBeanQueryNamespace(ClsEntityRepository.getBeanRepositoryNamespace()+"\\Query");
		
		EntityCls.setSqlQueryCls(getSqlQueryCls(cfg));
		EntityCls.setDatabase(cfg.getDatabase());
	
		Instruction.setStackTraceEnabled(cfg.isEnableStacktrace());
		
		Path pathModel = cfg.getModelPath();

		for (Table tbl : cfg.getEntityTables()) {

			List<OneToManyRelation> manyRelations = cfg.getOneToManyRelations(tbl);
			List<OneRelation> oneRelations = cfg.getOneRelations(tbl);
			List<ManyRelation> manyToManyRelations = cfg.getManyRelations(tbl);
			EntityCls cls = new EntityCls(tbl, manyRelations, oneRelations, manyToManyRelations);
			if(cfg.hasRenameMethodNames("EntityCls")) {
				cls.setRenameMethods(cfg.getRenameMethods("EntityCls"));
			}
			Entities.add(cls);
		}

		ClsEntityRepository repo = Types.EntityRepository;
		repo.addDeclarations(Entities.getAllEntities());

		for (EntityCls c : Entities.getAllEntities()) {
			c.addDeclarations();
		}

		Path pathBeans = pathModel.resolve("Entities");
		Path pathRepository = cfg.getRepositoryPath();
		Path helperPath = pathRepository.resolve("Helper");
		Path pathRepositoryQuery = pathRepository.resolve("Query");
		Path pathBeanPk = pathBeans.resolve("Pk");
		
		Files.createDirectories(pathBeanPk);
		Files.createDirectories(helperPath);
		Files.createDirectories(pathRepositoryQuery);
		
		try(DirectoryStream<Path> dsPathBeans = Files.newDirectoryStream(pathBeans)) {
			for(Path f : dsPathBeans) {
				if(!Entities.exists(StringUtil.dropAll(f.getFileName().toString(),".php")) && f.toString().endsWith(".php")) {
					Files.delete(f);
				}
			}
		} finally {
			
		}
	 
		try(DirectoryStream<Path> dsPathQuery = Files.newDirectoryStream(pathRepositoryQuery)) {
			for(Path f : dsPathQuery) {
				if(!Entities.exists(StringUtil.dropAll(f.getFileName().toString(),"EntityQuery.php")) && f.toString().endsWith(".php")) {
					Files.delete(f);
				}
			}
		} finally {
			
		}
		
		for (EntityCls c : Entities.getAllEntities()) {
			Path pathSrc = pathBeans.resolve(c.getName() + ".php");
			if (Files.exists(pathSrc)) {
				String existingSourceFile = new String(Files.readAllBytes(pathSrc), utf8);

				StringBuilder sbSrc = new StringBuilder();
				int startSrc = existingSourceFile.indexOf(BEGIN_CUSTOM_CLASS_MEMBERS);
				int endSrc = existingSourceFile.indexOf(END_CUSTOM_CLASS_MEMBERS,
						startSrc + BEGIN_CUSTOM_CLASS_MEMBERS.length());
				if (startSrc > -1 && endSrc > -1) {
					String implCode = existingSourceFile.substring(startSrc,
							endSrc + BEGIN_CUSTOM_CLASS_MEMBERS.length() - 1);
					c.addMethod(new CustomClassMemberCode(implCode));
					sbSrc.append(implCode).append('\n');
				}
				if (sbSrc.length() > 0)
					Files.write(Paths.get("bak_custom_class_members", pathSrc.getFileName().toString()),
							sbSrc.toString().getBytes(utf8), writeOptions);
			}
			c.addMethodImplementations();
		}
		repo.addMethodImplementations();
		
		

		for (EntityCls c : Entities.getAllEntities()) {
			if(c.getTbl().getPrimaryKey().isMultiColumn()) {
				Files.write(pathBeanPk.resolve("Pk"+ c.getName() + ".php"), ((PhpCls) c.getPkType()).toSourceString().getBytes(utf8), writeOptions);
			}
			
			Files.write(pathBeans.resolve(c.getName() + ".php"), c.toSourceString().getBytes(utf8), writeOptions);
			
			Files.write(helperPath.resolve(c.getFetchListHelperCls().getName() + ".php"), c.getFetchListHelperCls().toSourceString().getBytes(utf8), writeOptions);
			ClsEntityQuery clsQuery = new ClsEntityQuery(c);
			clsQuery.addMethodImplementations();
			Files.write(pathRepositoryQuery.resolve(clsQuery.getName() + ".php"),
					clsQuery.toSourceString().getBytes(utf8), writeOptions);
		}

		Files.write(pathRepository.resolve("EntityRepository.php"), repo.toSourceString().getBytes(utf8), writeOptions);

		if(cfg.getJsonMode() == OrmConfig.JsonMode.Server) {
			PhpJsonRestServer server = new PhpJsonRestServer( Entities.getAllEntities());
			server.addMethodImplementations();
			Files.write (cfg.getBasePath().resolve(server.getName()+ ".php"),
					server.toSourceString().getBytes(utf8), writeOptions);
		}
	}

}
