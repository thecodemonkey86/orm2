package generate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import config.ConfigReader;
import config.OrmConfig;
import config.SetPassConfigReader;
import cpp.JsonTypes;
import cpp.Types;
import cpp.bean.BeanCls;
import cpp.bean.Beans;
import cpp.beanquery.ClsBeanQuery;
import cpp.beanrepository.ClsBeanRepository;
import cpp.jsonentity.JsonEntities;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentityrepository.JsonEntityRepository;
import cpp.orm.DatabaseTypeMapper;
import cpp.orm.FirebirdDatabaseTypeMapper;
import cpp.orm.MySqlDatabaseMapper;
import cpp.orm.PgDatabaseTypeMapper;
import cpp.orm.SqliteDatabaseMapper;
import database.column.Column;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.table.Table;
import io.PasswordManager;
import xml.reader.DefaultXMLReader;

public class CppOrm extends OrmGenerator {
	
	private static final StandardOpenOption[] writeOptions={
			StandardOpenOption.WRITE,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING
			 
	};

	private DatabaseTypeMapper getTypeMapper(OrmConfig cfg) {
		if(cfg.isEngineFirebird()) {
			return new FirebirdDatabaseTypeMapper();
		} else if(cfg.isEnginePostgres()) {
			return new PgDatabaseTypeMapper();
		} else if(cfg.isEngineMysql()) {
			return new MySqlDatabaseMapper();
		} else if(cfg.isEngineSqlite()) {
			return new SqliteDatabaseMapper();
		} else {
			throw new RuntimeException("database not yet supported");
		}
	}
	
	public CppOrm(OrmConfig cfg) {
		super(cfg);
		HashSet<String> reservedNames = new HashSet<String>();
		reservedNames.add("private");
		Column.setReservedNames(reservedNames);
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
		if(setPass) {
			SetPassConfigReader cfgReader = new SetPassConfigReader();
			DefaultXMLReader.read(xmlFile, cfgReader);
			PasswordManager.saveToFile(cfgReader.getCredentials(), args[1] );
			return;
		}
		ConfigReader cfgReader = new ConfigReader(xmlFile.getParent());
		DefaultXMLReader.read(xmlFile, cfgReader);
		OrmConfig cfg=cfgReader.getCfg();
		new CppOrm(cfg).generate();
	}

	@Override
	public void generate() throws IOException 	{
Charset utf8 = Charset.forName("UTF-8");
		
		
		
		Path pathModel = cfg.getModelPath();
		if(cfg.getJsonMode() == null) {
			//default SQL mode
			BeanCls.setModelPath(cfg.getBasePath().relativize(cfg.getModelPath()).toString().replace('\\', '/'));
			BeanCls.setRepositoryPath(cfg.getBasePath().relativize(cfg.getRepositoryPath()).toString().replace('\\', '/'));
			BeanCls.setDatabase(cfg.getDatabase());
			BeanCls.setTypeMapper(getTypeMapper(cfg));

		
			for(Table tbl:cfg.getEntityTables() ) {
				
				List<OneToManyRelation> manyRelations = cfg.getOneToManyRelations(tbl);
				List<OneRelation> oneRelations = cfg.getOneRelations(tbl);
				List<ManyRelation> manyToManyRelations = cfg.getManyRelations(tbl);
	//			tableOneRelations.put(tbl, oneRelations);
	//			tableManyRelations.put(tbl, manyRelations);
				
				
				BeanCls cls = new BeanCls(tbl,manyRelations, oneRelations,manyToManyRelations);
				Beans.add(cls);
			}
			
			ClsBeanRepository repo=Types.BeanRepository;
			repo.addDeclarations(Beans.getAllBeans());
			for (BeanCls c : Beans.getAllBeans()) {
				c.setPrimaryKeyType();
			}
			
			for (BeanCls c : Beans.getAllBeans()) {
				c.addDeclarations();
			}
	//		for (BeanCls c : Beans.getAllBeans()) {
	//			c.breakPointerCircles();
	//		}
			Path pathBeans = pathModel.resolve("beans");
			for (BeanCls c : Beans.getAllBeans()) {
				Path pathHeader = pathBeans.resolve(c.getName().toLowerCase()+".h");
				Path pathSrc = pathBeans.resolve(c.getName().toLowerCase()+".cpp");
				
				
				if (Files.exists(pathHeader) && Files.exists(pathSrc)) {
					String existingHeaderFile = new String(Files.readAllBytes(pathHeader),utf8);
					String existingSourceFile = new String(Files.readAllBytes(pathSrc),utf8);
					
					Path pBackup  =Paths.get("bak_custom_class_members");
					int startHdr = -1;
					
					while((startHdr = existingHeaderFile.indexOf(BeanCls.BEGIN_CUSTOM_CLASS_MEMBERS,startHdr+1))>-1) {
						int endHdr = existingHeaderFile.indexOf(BeanCls.END_CUSTOM_CLASS_MEMBERS,startHdr);
						if(endHdr == -1) {
							throw new RuntimeException("Missing custom class members end marker: " + pathHeader);
						}
						String customClassMember = existingHeaderFile.substring(startHdr+BeanCls.BEGIN_CUSTOM_CLASS_MEMBERS.length(), endHdr);
						//c.addMethod(new CustomClassMemberCode(customClassMember, implCode) );
						
						if(!Files.exists(pBackup)) {
							Files.createDirectory(pBackup);
						}
						
						Files.write(pBackup.resolve(pathHeader.getFileName().toString()),customClassMember.getBytes(utf8), writeOptions);
						c.addCustomHeaderCode(customClassMember);
					}
					int startSrc = -1;
					
					while((startSrc = existingSourceFile.indexOf(BeanCls.BEGIN_CUSTOM_CLASS_MEMBERS,startSrc+1))>-1) {
						int endSrc = existingSourceFile.indexOf(BeanCls.END_CUSTOM_CLASS_MEMBERS,startSrc);
						if(endSrc == -1) {
							throw new RuntimeException("Missing custom class members end marker: " + pathSrc);
						}
						String implCode = existingSourceFile.substring(startSrc+BeanCls.BEGIN_CUSTOM_CLASS_MEMBERS.length(), endSrc);
						if(!Files.exists(pBackup)) {
							Files.createDirectory(pBackup);
						}
						Files.write(pBackup.resolve(pathSrc.getFileName().toString()),implCode.getBytes(utf8), writeOptions);
						
						//c.addMethod(new CustomClassMemberCode(customClassMember, implCode) );
						c.addCustomSourceCode(implCode);
					}
					startHdr = -1;
					while((startHdr = existingHeaderFile.indexOf(BeanCls.BEGIN_CUSTOM_PREPROCESSOR,startHdr+1))>-1) {
						int endHdr = existingHeaderFile.indexOf(BeanCls.END_CUSTOM_PREPROCESSOR,startHdr);
						if(endHdr == -1) {
							throw new RuntimeException("Missing custom preprocessor instructions end marker: " + pathHeader);
						}
						String customPp = existingHeaderFile.substring(startHdr+BeanCls.BEGIN_CUSTOM_PREPROCESSOR.length(), endHdr);
						c.addCustomPreprocessorCode(customPp );
					}
					
	//				if (sbHdr.length()>0)
	//					Files.write(Paths.get("bak_custom_class_members", pathHeader.getFileName().toString()),sbHdr.toString().getBytes(utf8), writeOptions);
	//				
	//				if (sbSrc.length()>0)
	//					Files.write(Paths.get("bak_custom_class_members", pathSrc.getFileName().toString()),sbSrc.toString().getBytes(utf8), writeOptions);
	//				
				}
				
				c.addMethodImplementations();
			}
			repo.addMethodImplementations();
	//		List<ManyRelation> list = tableManyRelations.get(getTableByName("artist"));
	//		System.out.println(list);
			
			
			Path pathRepository = cfg.getRepositoryPath();
			Path pathRepositoryQuery = pathRepository.resolve("query");
			Files.createDirectories(pathBeans);
			Files.createDirectories(pathRepositoryQuery);
	
			try(DirectoryStream<Path> dsPathBeans = Files.newDirectoryStream(pathBeans)) {
				for(Path f : dsPathBeans) {
					if(f.toString().endsWith(".h") || f.toString().endsWith(".cpp")) {
						Files.delete(f);
					}
				}
			} finally {
				
			}
			
			try(DirectoryStream<Path> dsPathQuery = Files.newDirectoryStream(pathRepositoryQuery)) {
				for(Path f : dsPathQuery) {
					if(f.toString().endsWith(".h") || f.toString().endsWith(".cpp")) {
						Files.delete(f);
					}
				}
			} finally {
				
			}
			
			
			for (BeanCls c : Beans.getAllBeans()) {
				
				Files.write(pathBeans.resolve(c.getName().toLowerCase()+".h"), c.toHeaderString().getBytes(utf8), writeOptions);
				Files.write(pathBeans.resolve(c.getName().toLowerCase()+".cpp"), c.toSourceString().getBytes(utf8), writeOptions);
				ClsBeanQuery clsQuery = new ClsBeanQuery(c);
				clsQuery.addMethodImplementations();
				Files.write(pathRepositoryQuery.resolve(clsQuery.getName().toLowerCase()+".h"), clsQuery.toHeaderString().getBytes(utf8), writeOptions);
				Files.write(pathRepositoryQuery.resolve(clsQuery.getName().toLowerCase()+".cpp"), clsQuery.toSourceString().getBytes(utf8), writeOptions);
			}
			
			Files.write(pathRepository.resolve("beanrepository.h"), repo.toHeaderString().getBytes(utf8), writeOptions);
			Files.write(pathRepository.resolve("beanrepository.cpp"), repo.toSourceString().getBytes(utf8), writeOptions);
		
//		BeanHelper helper = new BeanHelper(Beans.getAllBeans());
//		helper.addMethodImplementations();
//		Files.write(path.resolve("beanhelper").resolve("beanhelper.h"), helper.toHeaderString().getBytes(utf8), writeOptions);
//		Files.write(path.resolve("beanhelper").resolve("beanhelper.cpp"), helper.toSourceString().getBytes(utf8), writeOptions);
		} else {
			// json mode
			JsonEntity.setModelPath(cfg.getBasePath().relativize(cfg.getModelPath()).toString().replace('\\', '/'));
			JsonEntity.setRepositoryPath(cfg.getBasePath().relativize(cfg.getRepositoryPath()).toString().replace('\\', '/'));
			JsonEntity.setDatabase(cfg.getDatabase());
			JsonEntity.setTypeMapper(getTypeMapper(cfg));
			
			for(Table tbl:cfg.getEntityTables() ) {
				List<OneToManyRelation> oneToManyRelations = cfg.getOneToManyRelations(tbl);
				List<OneRelation> oneRelations = cfg.getOneRelations(tbl);
				List<ManyRelation> manyToManyRelations = cfg.getManyRelations(tbl);
				JsonEntity e = new JsonEntity(tbl, oneToManyRelations, oneRelations, manyToManyRelations);
				JsonEntities.add(e);
			}
			
			for (JsonEntity c : JsonEntities.getAllBeans()) {
				c.setPrimaryKeyType();
			}
			
			for (JsonEntity c : JsonEntities.getAllBeans()) {
				c.addDeclarations();
			}
			JsonEntityRepository repo = JsonTypes.JsonEntityRepository;
			repo.addDeclarations(JsonEntities.getAllBeans());

			repo.addMethodImplementations();
			for (JsonEntity c : JsonEntities.getAllBeans()) {
				c.addMethodImplementations();
			}
			
			
			Path pathBeans = pathModel.resolve("beans");
			Path pathRepository = cfg.getRepositoryPath();
			Path pathRepositoryQuery = pathRepository.resolve("query");
			Files.createDirectories(pathBeans);
			Files.createDirectories(pathRepositoryQuery);
	
			try(DirectoryStream<Path> dsPathBeans = Files.newDirectoryStream(pathBeans)) {
				for(Path f : dsPathBeans) {
					if(f.toString().endsWith(".h") || f.toString().endsWith(".cpp")) {
						Files.delete(f);
					}
				}
			} finally {
				
			}
			
			try(DirectoryStream<Path> dsPathQuery = Files.newDirectoryStream(pathRepositoryQuery)) {
				for(Path f : dsPathQuery) {
					if(f.toString().endsWith(".h") || f.toString().endsWith(".cpp")) {
						Files.delete(f);
					}
				}
			} finally {
				
			}
			
			for (JsonEntity c : JsonEntities.getAllBeans()) {
				Files.write(pathBeans.resolve(c.getName().toLowerCase()+".h"), c.toHeaderString().getBytes(utf8), writeOptions);
				Files.write(pathBeans.resolve(c.getName().toLowerCase()+".cpp"), c.toSourceString().getBytes(utf8), writeOptions);
			}
			Files.write(pathRepository.resolve(repo.getName().toLowerCase()+".h"), repo.toHeaderString().getBytes(utf8), writeOptions);
			Files.write(pathRepository.resolve(repo.getName().toLowerCase()+".cpp"), repo.toSourceString().getBytes(utf8), writeOptions);
			
		}
		
	}

}
