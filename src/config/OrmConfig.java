package config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import database.Database;
import database.DbCredentials;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.table.MappingTable;
import database.table.Table;
import util.Pair;

public class OrmConfig {
	public enum JsonMode {Server,Client}
	protected String basePath,pathModel, pathRepository;
	
	protected String dbEngine;
	protected List<Table> entityTables;
	protected List<MappingTable> mappingTables;
	protected Map<Table,List<OneToManyRelation>> oneToManyRelations;
	protected Map<Table,List<OneRelation>> oneRelations;
	protected Map<Table,List<ManyRelation>> manyToManyRelations;
	protected Map<String,List<Pair<String, String>>> renameMethods;
	protected Database database;
	private DbCredentials credentials;
	
	private boolean enableStacktrace = true;
	private boolean enableGetValueByName = false;
	private JsonMode jsonMode;
	private String overrideRepositoryClassName;
	public void setJsonMode(JsonMode jsonMode) {
		this.jsonMode = jsonMode;
	}
	
	public JsonMode getJsonMode() {
		return jsonMode;
	}
	
	public boolean isEnableStacktrace() {
		return enableStacktrace;
	}
	
	public OrmConfig() {
		this.entityTables = new ArrayList<>();
		this.mappingTables = new ArrayList<>();
		this.oneToManyRelations = new HashMap<>();
		this.oneRelations = new HashMap<>();
		this.manyToManyRelations = new HashMap<>();
		jsonMode = null;
	}
	
	
	public void setModelPath(String path) {
		this.pathModel = path;
	}
	
	
	public Path getModelPath() {
		if(pathModel == null ) {
			throw new RuntimeException("missing path \"model\"");
		}
		return getBasePath().resolve(pathModel);
	}


	
	public void setRepositoryPath(String pathRepository) {
		this.pathRepository = pathRepository;
	}
	
	public Path getRepositoryPath() {
		if(pathRepository == null ) {
			throw new RuntimeException("missing path \"model\"");
		}
		return getBasePath().resolve(pathRepository);
	}
	
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	
	public Path getBasePath() {
		if(basePath == null ) {
			throw new RuntimeException("missing base path");
		}
		return Paths.get(basePath);
	}
	
	public List<Table> getEntityTables() {
		return entityTables;
	}
	
	public List<MappingTable> getMappingTables() {
		return mappingTables;
	}
	
	public void addEntityTable(Table t) {
		this.entityTables.add(t);
		initRelations(t);
	}
	public void addOneToManyRelation(OneToManyRelation r,Table tbl) {
		List<OneToManyRelation> relations = this.oneToManyRelations.get(tbl);
		
		
		relations.add(r);
	}
	public void setDatabase(Database database) {
		this.database = database;
	}
	
	public Database getDatabase() {
		return database;
	}
	
	public List<OneToManyRelation> getOneToManyRelations(Table tbl) {
		return oneToManyRelations.get(tbl);
	}
	
	public Table getEntityTable(String name) {
		for(Table t : entityTables) {
			if(t.getName().equals(name)) {
				return t;
			}
		}
		throw new RuntimeException("no such table: " + name);
	}
	public List<OneRelation> getOneRelations(Table tbl) {
		return oneRelations.get(tbl);
	}
	public List<ManyRelation> getManyRelations(Table tbl) {
		return manyToManyRelations.get(tbl);
	}
	public void addMappingTable(MappingTable mappingTable) {
		this.mappingTables.add(mappingTable);
	}
	public void addManyToManyRelation(ManyRelation r, Table tbl) {
		List<ManyRelation> relations = this.manyToManyRelations.get(tbl);
		
		
		relations.add(r);
		
	}
	public void addOneRelation(OneRelation r, Table tbl) {
		List<OneRelation> relations = this.oneRelations.get(tbl);
		
		
		relations.add(r);
		
	}
	public MappingTable getMappingTable(String name) {
		for(MappingTable t : mappingTables) {
			if(t.getName().equals(name)) {
				return t;
			}
		}
		throw new RuntimeException("no such table: " + name);
	}
	private void initRelations(Table tbl) {
		oneRelations.put(tbl, new ArrayList<>());
		oneToManyRelations.put(tbl, new ArrayList<>());
		manyToManyRelations.put(tbl, new ArrayList<>());
		
	}
	
	public String getDbEngine() {
		return dbEngine;
	}
	
	public void setDbEngine(String dbEngine) {
		this.dbEngine = dbEngine;
	}
	
	public boolean isEnginePostgres() {
		return this.dbEngine.equals("postgres");
	}
	public boolean isEngineMysql() {
		return this.dbEngine.equals("mysql");
	}
	public boolean isEngineFirebird() {
		return this.dbEngine.equals("firebird");
	}
	public boolean isEngineMsSqlServer() {
		return this.dbEngine.equals("mssql");
	}

	public boolean isEngineSqlite() {
		return this.dbEngine.equals("sqlite");
	}

	boolean hasBasePath() {
		return basePath != null;
	}
	
	public DbCredentials getCredentials() {
		return credentials;
	}
	
	public void setCredentials(DbCredentials credentials) {
		this.credentials = credentials;
	}
	
	public void addRenameMethod(String cls, String oldname,String newname) {
		if(renameMethods == null) {
			renameMethods = new HashMap<>();
		}
		if(!renameMethods.containsKey(cls)) {
			renameMethods.put(cls, new ArrayList<>());
		}
		renameMethods.get(cls).add(new Pair<String,String>(oldname, newname));
	}
	
	public List<Pair<String, String>> getRenameMethods(String cls) {
		return renameMethods.get(cls);
	}

	public boolean hasRenameMethodNames(String cls) {
		return renameMethods != null && renameMethods.containsKey(cls);
	}

	public void removeTable(String tbl) {
		Iterator<Table> iterator = entityTables.iterator();
		while(iterator.hasNext()) {
			if(iterator.next().getName().equals(tbl)) {
				iterator.remove();
				break;
			}
		}
		
		Iterator<Table> iterator2 = oneRelations.keySet().iterator();
		while(iterator2.hasNext()) {
			Table t = iterator2.next();
			if(t.getName().equals(tbl)) {
				iterator2.remove();
			} else {
			
				Iterator<OneRelation> iterator3 = oneRelations.get(t).iterator();
				
				while(iterator3.hasNext()) {
					if(iterator3.next().getDestTable().getName().equals(tbl)) {
						iterator3.remove();
					}
				}
			}
		}
		
		iterator2 = oneToManyRelations.keySet().iterator();
		while(iterator2.hasNext()) {
			Table t = iterator2.next();
			if(t.getName().equals(tbl)) {
				iterator2.remove();
			} else {
			
				Iterator<OneToManyRelation> iterator3 = oneToManyRelations.get(t).iterator();
				
				while(iterator3.hasNext()) {
					if(iterator3.next().getDestTable().getName().equals(tbl)) {
						iterator3.remove();
					}
				}
			}
		}
		
		iterator2 = manyToManyRelations.keySet().iterator();
		while(iterator2.hasNext()) {
			Table t = iterator2.next();
			if(t.getName().equals(tbl)) {
				iterator2.remove();
			} else {
				
				Iterator<ManyRelation> iterator3 = manyToManyRelations.get(t).iterator();
				
				while(iterator3.hasNext()) {
					if(iterator3.next().getDestTable().getName().equals(tbl)) {
						iterator3.remove();
					}
				}
			}
		}
		
		
	}
	
	public void setEnableGetValueByName(boolean enableGetValueByName) {
		this.enableGetValueByName = enableGetValueByName;
	}
	
	public boolean isEnableGetValueByName() {
		return enableGetValueByName;
	}

	public void setOverrideRepositoryClassName(String overrideRepositoryClassName) {
		this.overrideRepositoryClassName = overrideRepositoryClassName;
	}
	
	public String getOverrideRepositoryClassName() {
		return overrideRepositoryClassName;
	}
	
	public boolean hasOverrideRepositoryClassName() {
		return overrideRepositoryClassName!=null;
	}
}
