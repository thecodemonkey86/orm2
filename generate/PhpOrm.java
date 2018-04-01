package generate;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import config.ConfigReader;
import config.OrmConfig;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.table.Table;
import php.bean.BeanCls;
import php.bean.Beans;
import php.bean.CustomClassMemberCode;
import php.beanrepository.ClsBeanRepository;
import php.beanrepository.query.ClsBeanQuery;
import php.core.PhpCls;
import php.core.Types;
import php.core.instruction.InstructionBlock;
import php.orm.DatabaseTypeMapper;
import php.orm.MySqlDatabaseMapper;
import php.orm.PgDatabaseMapper;
import xml.reader.DefaultXMLReader;

public class PhpOrm extends OrmCommon {
	private static final String BEGIN_CUSTOM_CLASS_MEMBERS = "/*BEGIN_CUSTOM_CLASS_MEMBERS*/";
	private static final String END_CUSTOM_CLASS_MEMBERS = "/*END_CUSTOM_CLASS_MEMBERS*/";

	private static final StandardOpenOption[] writeOptions = { StandardOpenOption.WRITE, StandardOpenOption.CREATE,
			StandardOpenOption.TRUNCATE_EXISTING

	};

	private DatabaseTypeMapper getTypeMapper(OrmConfig cfg) {
		if (cfg.isEnginePostgres()) {
			return new PgDatabaseMapper();
		} else if (cfg.isEngineMysql()) {
			return new MySqlDatabaseMapper();
		} else {
			throw new RuntimeException("database not yet supported");
		}
	}

	private PhpCls getSqlQueryCls(OrmConfig cfg) {
		if (cfg.isEnginePostgres()) {
			return Types.PgSqlQuery;
		} else if (cfg.isEngineMysql()) {
			return Types.MysqlSqlQuery;
		} else {
			throw new RuntimeException("database not yet supported");
		}
	}

	public PhpOrm(OrmConfig cfg) throws Exception {
		super(cfg);
		Charset utf8 = Charset.forName("UTF-8");
		ClsBeanRepository.setBeanRepositoryNamespace(cfg.getBasePath().relativize(cfg.getRepositoryPath()).toString().replace("/", "\\"));
		BeanCls.setBeanNamespace(cfg.getBasePath().relativize(cfg.getModelPath()).toString().replace("/", "\\")+"\\Beans");
		BeanCls.setBeanRepoNamespace(cfg.getBasePath().relativize(cfg.getRepositoryPath()).toString().replace("/", "\\"));
		ClsBeanQuery.setBeanQueryNamespace(ClsBeanRepository.getBeanRepositoryNamespace()+"\\Query");
		
		BeanCls.setSqlQueryCls(getSqlQueryCls(cfg));
		BeanCls.setDatabase(cfg.getDatabase());
		BeanCls.setTypeMapper(getTypeMapper(cfg));
		InstructionBlock.setEnableStacktrace(cfg.isEnableStacktrace());
		
		Path pathModel = cfg.getModelPath();

		for (Table tbl : cfg.getEntityTables()) {

			List<OneToManyRelation> manyRelations = cfg.getOneToManyRelations(tbl);
			List<OneRelation> oneRelations = cfg.getOneRelations(tbl);
			List<ManyRelation> manyToManyRelations = cfg.getManyRelations(tbl);
			BeanCls cls = new BeanCls(tbl, manyRelations, oneRelations, manyToManyRelations);
			Beans.add(cls);
		}

		ClsBeanRepository repo = Types.BeanRepository;
		repo.addDeclarations(Beans.getAllBeans());

		for (BeanCls c : Beans.getAllBeans()) {
			c.addDeclarations();
		}

		Path pathBeans = pathModel.resolve("Beans");
		for (BeanCls c : Beans.getAllBeans()) {
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
		
		Path pathRepository = cfg.getRepositoryPath();
		Path helperPath = pathRepository.resolve("Helper");
		Path pathRepositoryQuery = pathRepository.resolve("Query");
		Path pathBeanPk = pathBeans.resolve("Pk");
		Files.createDirectories(pathBeanPk);
		Files.createDirectories(helperPath);
		Files.createDirectories(pathRepositoryQuery);

		for (BeanCls c : Beans.getAllBeans()) {
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

		Files.write(pathRepository.resolve("BeanRepository.php"), repo.toSourceString().getBytes(utf8), writeOptions);

	}

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			throw new Exception("Please provide xml config file");
		}
		Path xmlFile = Paths.get(args[0]);
		ConfigReader cfgReader = new ConfigReader(xmlFile.getParent());
		DefaultXMLReader.read(xmlFile, cfgReader);
		OrmConfig cfg = cfgReader.getCfg();
		new PhpOrm(cfg);
		
	}

}
