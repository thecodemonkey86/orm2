package generate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import config.OrmConfig;
import config.SetPassConfigReader;
import config.php.PhpConfigReader;
import config.php.PhpOrmConfig;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.table.Table;
import io.PasswordManager;
import php.Php;
import php.bean.EntityCls;
import php.bean.Entities;
import php.bean.CustomClassMemberCode;
import php.beanrepository.ClsBeanRepository;
import php.beanrepository.query.ClsBeanQuery;
import php.core.PhpCls;
import php.core.Types;
import php.core.instruction.InstructionBlock;
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
		if (args.length == 0) {
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
		if(setPass) {
			SetPassConfigReader cfgReader = new SetPassConfigReader();
			DefaultXMLReader.read(xmlFile, cfgReader);
			PasswordManager.saveToFile(cfgReader.getCredentials(), args[1] );
			return;
		}
		PhpConfigReader cfgReader = new PhpConfigReader(xmlFile.getParent());
		DefaultXMLReader.read(xmlFile, cfgReader);
		PhpOrmConfig cfg = cfgReader.getCfg();
		new PhpOrm(cfg).generate();
		
	}

	@Override
	public void generate() throws IOException  {
		Php.phpVersion = ((PhpOrmConfig) cfg).getPhpversion();
		EntityCls.setTypeMapper(getTypeMapper(cfg));
		Charset utf8 = Charset.forName("UTF-8");
		ClsBeanRepository.setBeanRepositoryNamespace(cfg.getBasePath().relativize(cfg.getRepositoryPath()).toString().replace("/", "\\"));
		EntityCls.setBeanNamespace(cfg.getBasePath().relativize(cfg.getModelPath()).toString().replace("/", "\\")+"\\Entities");
		EntityCls.setBeanRepoNamespace(cfg.getBasePath().relativize(cfg.getRepositoryPath()).toString().replace("/", "\\"));
		ClsBeanQuery.setBeanQueryNamespace(ClsBeanRepository.getBeanRepositoryNamespace()+"\\Query");
		
		EntityCls.setSqlQueryCls(getSqlQueryCls(cfg));
		EntityCls.setDatabase(cfg.getDatabase());
	
		InstructionBlock.setEnableStacktrace(cfg.isEnableStacktrace());
		
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

		ClsBeanRepository repo = Types.BeanRepository;
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
			ClsBeanQuery clsQuery = new ClsBeanQuery(c);
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
