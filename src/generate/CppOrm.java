package generate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import config.OrmConfig;
import config.SetPassConfigReader;
import config.cpp.CppConfigReader;
import config.cpp.CppOrmConfig;
import cpp.JsonTypes;
import cpp.Types;
import cpp.core.instruction.Instruction;
import cpp.entity.Entities;
import cpp.entity.EntityCls;
import cpp.entityquery.ClsEntityQueryDelete;
import cpp.entityquery.ClsEntityQuerySelect;
import cpp.entityquery.ClsEntityQueryUpdate;
import cpp.entityrepository.ClsEntityRepository;
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
import util.FileUtil2;
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
		CppConfigReader cfgReader = new CppConfigReader(xmlFile.getParent());
		DefaultXMLReader.read(xmlFile, cfgReader);
		OrmConfig cfg=cfgReader.getCfg();
		new CppOrm(cfg).generate();
	}

	@Override
	public void generate() throws IOException 	{
		CppOrmConfig cfg = (CppOrmConfig) this.cfg;
		EntityCls.setCfg(cfg);
		Charset utf8 = Charset.forName("UTF-8");
		Instruction.setStackTraceEnabled(cfg.isEnableStacktrace());
		
		
		Path pathModel = cfg.getModelPath();
		if(cfg.getJsonMode() == null) {
			//default SQL mode
			EntityCls.setModelPath(cfg.getBasePath().relativize(cfg.getModelPath()).toString().replace('\\', '/'));
			EntityCls.setRepositoryPath(cfg.getBasePath().relativize(cfg.getRepositoryPath()).toString().replace('\\', '/'));
			EntityCls.setDatabase(cfg.getDatabase());
			EntityCls.setTypeMapper(getTypeMapper(cfg));

		
			for(Table tbl:cfg.getEntityTables() ) {
				
				List<OneToManyRelation> manyRelations = cfg.getOneToManyRelations(tbl);
				List<OneRelation> oneRelations = cfg.getOneRelations(tbl);
				List<ManyRelation> manyToManyRelations = cfg.getManyRelations(tbl);
	//			tableOneRelations.put(tbl, oneRelations);
	//			tableManyRelations.put(tbl, manyRelations);
				
				
				EntityCls cls = new EntityCls(tbl,manyRelations, oneRelations,manyToManyRelations);
				if(cfg.hasValidators(tbl.getName())) {
					cls.setColumnValidators(cfg.getValidators(tbl.getName()));
				}
//				if(cfg.hasNamespace()) {
//					cls.setUseNamespace(cfg.getNamespace());
//				}
				Entities.add(cls);
			}
			
			ClsEntityRepository repo=Types.EntityRepository;
			if(cfg.hasOverrideRepositoryClassName()) {
				repo.setName(cfg.getOverrideRepositoryClassName());
			}
			repo.addDeclarations(Entities.getAllBeans());
			for (EntityCls c : Entities.getAllBeans()) {
				c.setPrimaryKeyType();
			}
			
			for (EntityCls c : Entities.getAllBeans()) {
				c.addDeclarations();
			}
	//		for (BeanCls c : Beans.getAllBeans()) {
	//			c.breakPointerCircles();
	//		}
			Path pathBeans = pathModel.resolve("entities");
			for (EntityCls c : Entities.getAllBeans()) {
				Path pathHeader = pathBeans.resolve(c.getName().toLowerCase()+".h");
				Path pathSrc = pathBeans.resolve(c.getName().toLowerCase()+".cpp");
				
				
				if (Files.exists(pathHeader) && Files.exists(pathSrc)) {
					String existingHeaderFile = new String(Files.readAllBytes(pathHeader),utf8);
					String existingSourceFile = new String(Files.readAllBytes(pathSrc),utf8);
					
					Path pBackup  =Paths.get("bak_custom_class_members");
					if(!Files.exists(pBackup)) {
						Files.createDirectory(pBackup);
					}
					
					FileUtil2.writeFileIfContentChanged(pBackup.resolve(pathHeader.getFileName().toString()),existingHeaderFile.getBytes(utf8), writeOptions);
					FileUtil2.writeFileIfContentChanged(pBackup.resolve(pathSrc.getFileName().toString()),existingSourceFile.getBytes(utf8), writeOptions);
					
					int startHdr = -1;
					
					while((startHdr = existingHeaderFile.indexOf(EntityCls.BEGIN_CUSTOM_CLASS_MEMBERS,startHdr+1))>-1) {
						int endHdr = existingHeaderFile.indexOf(EntityCls.END_CUSTOM_CLASS_MEMBERS,startHdr);
						if(endHdr == -1) {
							throw new RuntimeException("Missing custom class members end marker: " + pathHeader);
						}
						String customClassMember = existingHeaderFile.substring(startHdr+EntityCls.BEGIN_CUSTOM_CLASS_MEMBERS.length(), endHdr);
						//c.addMethod(new CustomClassMemberCode(customClassMember, implCode) );
						
						c.addCustomHeaderCode(customClassMember);
					}
					int startSrc = -1;
					
					while((startSrc = existingSourceFile.indexOf(EntityCls.BEGIN_CUSTOM_CLASS_MEMBERS,startSrc+1))>-1) {
						int endSrc = existingSourceFile.indexOf(EntityCls.END_CUSTOM_CLASS_MEMBERS,startSrc);
						if(endSrc == -1) {
							throw new RuntimeException("Missing custom class members end marker: " + pathSrc);
						}
						String implCode = existingSourceFile.substring(startSrc+EntityCls.BEGIN_CUSTOM_CLASS_MEMBERS.length(), endSrc);
						if(!Files.exists(pBackup)) {
							Files.createDirectory(pBackup);
						}
						//FileUtil2.writeFileIfContentChanged(pBackup.resolve(pathSrc.getFileName().toString()),implCode.getBytes(utf8), writeOptions);
						
						//c.addMethod(new CustomClassMemberCode(customClassMember, implCode) );
						c.addCustomSourceCode(implCode);
					}
					startHdr = -1;
					while((startHdr = existingHeaderFile.indexOf(EntityCls.BEGIN_CUSTOM_PREPROCESSOR,startHdr+1))>-1) {
						int endHdr = existingHeaderFile.indexOf(EntityCls.END_CUSTOM_PREPROCESSOR,startHdr);
						if(endHdr == -1) {
							throw new RuntimeException("Missing custom preprocessor instructions end marker: " + pathHeader);
						}
						String customPp = existingHeaderFile.substring(startHdr+EntityCls.BEGIN_CUSTOM_PREPROCESSOR.length(), endHdr);
						c.addCustomPreprocessorCode(customPp );
					}
					
	//				if (sbHdr.length()>0)
	//					FileUtil2.writeFileIfContentChanged(Paths.get("bak_custom_class_members", pathHeader.getFileName().toString()),sbHdr.toString().getBytes(utf8), writeOptions);
	//				
	//				if (sbSrc.length()>0)
	//					FileUtil2.writeFileIfContentChanged(Paths.get("bak_custom_class_members", pathSrc.getFileName().toString()),sbSrc.toString().getBytes(utf8), writeOptions);
	//				
				}
				c.setExportMacro(cfg.getExportMacro(),cfg.getExportMacroIncludeHeader());
				c.addMethodImplementations();
			}
			repo.setExportMacro(cfg.getExportMacro(),cfg.getExportMacroIncludeHeader());
			repo.addMethodImplementations();
//			if(cfg.hasNamespace()) {
//				repo.setUseNamespace(cfg.getNamespace());
//			}
	//		List<ManyRelation> list = tableManyRelations.get(getTableByName("artist"));
	//		System.out.println(list);
			
			
			Path pathRepository = cfg.getRepositoryPath();
			Path pathRepositoryQuery = pathRepository.resolve("query");
			Files.createDirectories(pathBeans);
			Files.createDirectories(pathRepositoryQuery);
	
			/*try(DirectoryStream<Path> dsPathBeans = Files.newDirectoryStream(pathBeans)) {
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
				
			}*/
//			FileUtil2.writeFileIfContentChanged(pathRepositoryQuery.resolve(EnumQueryMode.INSTANCE.getName().toLowerCase()+".h"), EnumQueryMode.INSTANCE.toHeaderString().getBytes(utf8), writeOptions);
			
			for (EntityCls c : Entities.getAllBeans()) {
				
				FileUtil2.writeFileIfContentChanged(pathBeans.resolve(c.getName().toLowerCase()+".h"), c.toHeaderString().getBytes(utf8), writeOptions);
				FileUtil2.writeFileIfContentChanged(pathBeans.resolve(c.getName().toLowerCase()+".cpp"), c.toSourceString().getBytes(utf8), writeOptions);
				
				
				ClsEntityQuerySelect clsQuery = Types.beanQuerySelect(c);
				clsQuery.setExportMacro(cfg.getExportMacro(),cfg.getExportMacroIncludeHeader());
				clsQuery.addMethodImplementations();
//				if(cfg.hasNamespace()) {
//					clsQuery.setUseNamespace(cfg.getNamespace());
//				}
				FileUtil2.writeFileIfContentChanged(pathRepositoryQuery.resolve(clsQuery.getName().toLowerCase()+".h"), clsQuery.toHeaderString().getBytes(utf8), writeOptions);
				FileUtil2.writeFileIfContentChanged(pathRepositoryQuery.resolve(clsQuery.getName().toLowerCase()+".cpp"), clsQuery.toSourceString().getBytes(utf8), writeOptions);
				
				if(c.getTbl().hasQueryType(Table.QueryType.Delete)) {
					ClsEntityQueryDelete clsDelete = new ClsEntityQueryDelete(c);
					clsDelete.setExportMacro(cfg.getExportMacro(),cfg.getExportMacroIncludeHeader());
//					if(cfg.hasNamespace()) {
//						clsDelete.setUseNamespace(cfg.getNamespace());
//					}
					clsDelete.addMethodImplementations();
					FileUtil2.writeFileIfContentChanged(pathRepositoryQuery.resolve(clsDelete.getName().toLowerCase()+".h"), clsDelete.toHeaderString().getBytes(utf8), writeOptions);
					FileUtil2.writeFileIfContentChanged(pathRepositoryQuery.resolve(clsDelete.getName().toLowerCase()+".cpp"), clsDelete.toSourceString().getBytes(utf8), writeOptions);
				}
				if(c.getTbl().hasQueryType(Table.QueryType.Update)) {
					ClsEntityQueryUpdate clsUpdate = new ClsEntityQueryUpdate(c);
					clsUpdate.setExportMacro(cfg.getExportMacro(),cfg.getExportMacroIncludeHeader());
//					if(cfg.hasNamespace()) {
//						clsUpdate.setUseNamespace(cfg.getNamespace());
//					}
					clsUpdate.addMethodImplementations();
					FileUtil2.writeFileIfContentChanged(pathRepositoryQuery.resolve(clsUpdate.getName().toLowerCase()+".h"), clsUpdate.toHeaderString().getBytes(utf8), writeOptions);
					FileUtil2.writeFileIfContentChanged(pathRepositoryQuery.resolve(clsUpdate.getName().toLowerCase()+".cpp"), clsUpdate.toSourceString().getBytes(utf8), writeOptions);
				}
			}
			
			FileUtil2.writeFileIfContentChanged(pathRepository.resolve(repo.getName().toLowerCase()+ ".h"), repo.toHeaderString().getBytes(utf8), writeOptions);
			FileUtil2.writeFileIfContentChanged(pathRepository.resolve(repo.getName().toLowerCase()+".cpp"), repo.toSourceString().getBytes(utf8), writeOptions);
		
//		BeanHelper helper = new BeanHelper(Beans.getAllBeans());
//		helper.addMethodImplementations();
//		FileUtil2.writeFileIfContentChanged(path.resolve("entityhelper").resolve("entityhelper.h"), helper.toHeaderString().getBytes(utf8), writeOptions);
//		FileUtil2.writeFileIfContentChanged(path.resolve("entityhelper").resolve("entityhelper.cpp"), helper.toSourceString().getBytes(utf8), writeOptions);
		} else {
			Path pathEntities = pathModel.resolve("entities");
			Path pathRepository = cfg.getRepositoryPath();
			Path pathRepositoryQuery = pathRepository.resolve("query");
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
			
			for (JsonEntity c : JsonEntities.getAllEntities()) {
				c.setPrimaryKeyType();
			}
			
			for (JsonEntity c : JsonEntities.getAllEntities()) {
				c.addDeclarations();
			}
			JsonEntityRepository repo = JsonTypes.JsonEntityRepository;
			repo.addDeclarations(JsonEntities.getAllEntities());
			repo.setExportMacro(cfg.getExportMacro(),cfg.getExportMacroIncludeHeader());
			repo.addMethodImplementations();
			for (JsonEntity c : JsonEntities.getAllEntities()) {
				Path pathHeader = pathEntities.resolve(c.getName().toLowerCase()+".h");
				Path pathSrc = pathEntities.resolve(c.getName().toLowerCase()+".cpp");
				
				
				if (Files.exists(pathHeader) && Files.exists(pathSrc)) {
					String existingHeaderFile = new String(Files.readAllBytes(pathHeader),utf8);
					String existingSourceFile = new String(Files.readAllBytes(pathSrc),utf8);
					Path pBackup  =Paths.get("bak_custom_class_members");
					if(!Files.exists(pBackup)) {
						Files.createDirectory(pBackup);
					}
					
					FileUtil2.writeFileIfContentChanged(pBackup.resolve(pathHeader.getFileName().toString()),existingHeaderFile.getBytes(utf8), writeOptions);
					FileUtil2.writeFileIfContentChanged(pBackup.resolve(pathSrc.getFileName().toString()),existingSourceFile.getBytes(utf8), writeOptions);
					
					
					int startHdr = -1;
					
					while((startHdr = existingHeaderFile.indexOf(EntityCls.BEGIN_CUSTOM_CLASS_MEMBERS,startHdr+1))>-1) {
						int endHdr = existingHeaderFile.indexOf(EntityCls.END_CUSTOM_CLASS_MEMBERS,startHdr);
						if(endHdr == -1) {
							throw new RuntimeException("Missing custom class members end marker: " + pathHeader);
						}
						String customClassMember = existingHeaderFile.substring(startHdr+EntityCls.BEGIN_CUSTOM_CLASS_MEMBERS.length(), endHdr);
						//c.addMethod(new CustomClassMemberCode(customClassMember, implCode) );
						
						
						c.addCustomHeaderCode(customClassMember);
					}
					int startSrc = -1;
					
					while((startSrc = existingSourceFile.indexOf(EntityCls.BEGIN_CUSTOM_CLASS_MEMBERS,startSrc+1))>-1) {
						int endSrc = existingSourceFile.indexOf(EntityCls.END_CUSTOM_CLASS_MEMBERS,startSrc);
						if(endSrc == -1) {
							throw new RuntimeException("Missing custom class members end marker: " + pathSrc);
						}
						String implCode = existingSourceFile.substring(startSrc+EntityCls.BEGIN_CUSTOM_CLASS_MEMBERS.length(), endSrc);
						if(!Files.exists(pBackup)) {
							Files.createDirectory(pBackup);
						}
					//	FileUtil2.writeFileIfContentChanged(pBackup.resolve(pathSrc.getFileName().toString()),implCode.getBytes(utf8), writeOptions);
						
						//c.addMethod(new CustomClassMemberCode(customClassMember, implCode) );
						c.addCustomSourceCode(implCode);
					}
					startHdr = -1;
					while((startHdr = existingHeaderFile.indexOf(EntityCls.BEGIN_CUSTOM_PREPROCESSOR,startHdr+1))>-1) {
						int endHdr = existingHeaderFile.indexOf(EntityCls.END_CUSTOM_PREPROCESSOR,startHdr);
						if(endHdr == -1) {
							throw new RuntimeException("Missing custom preprocessor instructions end marker: " + pathHeader);
						}
						String customPp = existingHeaderFile.substring(startHdr+EntityCls.BEGIN_CUSTOM_PREPROCESSOR.length(), endHdr);
						c.addCustomPreprocessorCode(customPp );
					}
				}
				c.setExportMacro(cfg.getExportMacro(),cfg.getExportMacroIncludeHeader());
				c.addMethodImplementations();
			}
			
			
			
			Files.createDirectories(pathEntities);
			Files.createDirectories(pathRepositoryQuery);
	
			/*try(DirectoryStream<Path> dsPathBeans = Files.newDirectoryStream(pathEntities)) {
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
				
			}*/
			
			for (JsonEntity c : JsonEntities.getAllEntities()) {
				FileUtil2.writeFileIfContentChanged(pathEntities.resolve(c.getName().toLowerCase()+".h"), c.toHeaderString().getBytes(utf8), writeOptions);
				FileUtil2.writeFileIfContentChanged(pathEntities.resolve(c.getName().toLowerCase()+".cpp"), c.toSourceString().getBytes(utf8), writeOptions);
			}
			FileUtil2.writeFileIfContentChanged(pathRepository.resolve(repo.getName().toLowerCase()+".h"), repo.toHeaderString().getBytes(utf8), writeOptions);
			FileUtil2.writeFileIfContentChanged(pathRepository.resolve(repo.getName().toLowerCase()+".cpp"), repo.toSourceString().getBytes(utf8), writeOptions);
			
		}
		
	}

}
