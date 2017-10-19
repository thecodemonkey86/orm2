package generate; 

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import sunjava.Types;
import sunjava.cls.JavaCls;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.bean.Beans;
import sunjava.cls.bean.CustomClassMemberCode;
import sunjava.cls.bean.repo.ClsBeanRepository;
import sunjava.cls.bean.repo.query.ClsBeanQuery;
import sunjava.config.JavaConfigReader;
import sunjava.config.JavaOrmOutputConfig;
import sunjava.orm.PgDatabaseMapper;
import model.Column;
import model.ManyRelation;
import model.OneToManyRelation;
import model.OneRelation;
import model.PgDatabase;
import model.Table;
import xml.reader.DefaultXMLReader;

public class JavaOrm extends OrmCommon{
	private static final String BEGIN_CUSTOM_CLASS_MEMBERS = "/*BEGIN_CUSTOM_CLASS_MEMBERS*/";
	private static final String END_CUSTOM_CLASS_MEMBERS = "/*END_CUSTOM_CLASS_MEMBERS*/";
	
	private static final StandardOpenOption[] writeOptions={
		StandardOpenOption.CREATE,StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING
			
	};

	
	public JavaOrm(JavaOrmOutputConfig cfg) throws Exception {
		super(cfg);
		Charset utf8 = Charset.forName("UTF-8");
		Class.forName("org.postgresql.Driver");

		ClsBeanQuery.setBeanQueryPackage(cfg.getRepositoryPackageName()+".query");
		BeanCls.setBeanClsPackage(cfg.getBeanPackageName());
		ClsBeanRepository.setBeanRepositoryPackage(cfg.getRepositoryPackageName());
		
		BeanCls.setDatabase(new PgDatabase(cfg.getDbName(), "public"));
		BeanCls.setTypeMapper(new PgDatabaseMapper());
		BeanCls.setSqlQueryCls(Types.PgSqlQuery);
		Column.setColumnEscapeChar(BeanCls.getDatabase().getColumnEscapeChar());
		Path path = cfg.getBasePath();
		Path modelPath = cfg.getModelPath();
		Path repositoryPath = cfg.getRepositoryPath();
		Path beansPath = modelPath.resolve("beans");
		Path fetchListHelperPath = beansPath.resolve("helper");
		
		
		ClsBeanRepository repo=Types.BeanRepository;
		
		for(Table tbl:cfg.getEntityTables()  ) {
			
			List<OneToManyRelation> manyRelations = cfg.getOneToManyRelations(tbl);
			List<OneRelation> oneRelations = cfg.getOneRelations(tbl);
			List<ManyRelation> manyToManyRelations = cfg.getManyRelations(tbl);
//			tableOneRelations.put(tbl, oneRelations);
//			tableManyRelations.put(tbl, manyRelations);
			
			
			BeanCls cls = new BeanCls(tbl,manyRelations, oneRelations,manyToManyRelations);
			Beans.add(cls);
		}
		
		
		for (BeanCls c : Beans.getAllBeans()) {
			c.addDeclarations();
		}
		
		repo.addDeclarations(Beans.getAllBeans());
//		for (BeanCls c : Beans.getAllBeans()) {
//			c.breakPointerCircles();
//		}
		for (BeanCls c : Beans.getAllBeans()) {
			//Path pathHeader = Paths.get(path+"/beans/"+c.getName().toLowerCase()+".h");
			Path pathSrc = Paths.get(path+"/beans/"+c.getName()+".java");
			
			
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
//		List<ManyRelation> list = tableManyRelations.get(getTableByName("artist"));
//		System.out.println(list);
		
		Files.createDirectories(fetchListHelperPath);
		Files.createDirectories(repositoryPath.resolve("query"));
		
		for (BeanCls c : Beans.getAllBeans()) {
			Path pathSrc = beansPath.resolve(c.getName()+".java");
			Path pathHelperSrc = fetchListHelperPath.resolve(c.getFetchListHelperCls().getName()+".java");
					
			Files.write(pathSrc, c.toSourceString().getBytes(utf8), writeOptions);
			Files.write(pathHelperSrc, c.getFetchListHelperCls().toSourceString().getBytes(utf8), writeOptions);
			ClsBeanQuery clsQuery = new ClsBeanQuery(c);
			clsQuery.addMethodImplementations();
			Files.write(repositoryPath.resolve("query").resolve(clsQuery.getName()+".java"), clsQuery.toSourceString().getBytes(utf8), writeOptions);
			
			if(c.getTbl().getPrimaryKey().isMultiColumn()) {
				Path pathMultiColumnPkType = beansPath.resolve("pk").resolve(c.getPkType().getName()+".java");
				Files.createDirectories(beansPath.resolve("pk"));
				Files.write(pathMultiColumnPkType, ((JavaCls) c.getPkType()).toSourceString().getBytes(utf8), writeOptions);
			}
		}
		
		Files.write(repositoryPath.resolve("BeanRepository.java"), repo.toSourceString().getBytes(utf8), writeOptions);
		
	}

	public static void main(String[] args) throws Exception {
		if(args.length == 0) {
			throw new Exception("Please provide xml config file");
		}
		JavaConfigReader cfgReader = new JavaConfigReader();
		DefaultXMLReader.read(Paths.get(args[0]), cfgReader);
		JavaOrmOutputConfig cfg=cfgReader.getCfg();
		new JavaOrm(cfg);
	}

}
