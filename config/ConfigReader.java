package config;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashSet;
import java.util.Properties;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import database.Database;
import database.DbCredentials;
import database.FirebirdCredentials;
import database.FirebirdDatabase;
import database.MySqlCredentials;
import database.MySqlDatabase;
import database.PgDatabase;
import database.SqliteCredentials;
import database.SqliteDatabase;
import database.PgCredentials;
import database.column.Column;
import database.relation.M2MColumns;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.relation.PrimaryKey;
import database.table.MappingTable;
import database.table.Table;
import util.Pair;

public class ConfigReader implements ContentHandler {

	protected Connection conn;
	protected OrmConfig cfg;
	
	protected String tag;

	private enum Section {
		ENTITIES, MAPPING_TABLES, ONE_TO_MANY_RELATIONS, MANY_TO_MANY_RELATIONS, ONE_RELATIONS
	}

	private Table currentEntityTable;
	private int oneToManyAliasCounter = 1;
	private int manyToManyAliasCounter = 1;
	private int oneAliasCounter = 1;
	private HashSet<String> relationQueryNames;
	private OneToManyRelation currentOneToManyRelation;
	private OneRelation currentOneRelation;
	private ManyRelation currentManyToManyRelation;
	private Table currentSrcTable;
	private Table currentDestTable;
	private M2MColumns currentSourceEntityMapping;
	private M2MColumns currentDestEntityMapping;
	private MappingTable currentMappingTable;
	private Section section;
	
	
	public OrmConfig getCfg() {
		return cfg;
	}

	public ConfigReader() {
		createConfig();
		relationQueryNames = new HashSet<>();
	}

	protected void createConfig() {
		cfg = new OrmConfig();
	}

	protected String checkRelationQueryNameUnique(String name) throws IOException {
		if (!relationQueryNames.contains(name)) {
			relationQueryNames.add(name);
			return name;
		}
		throw new IOException(String.format("relation name must be unique (%s)", name));
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		try {
			this.tag = localName;
			switch (this.tag) {
			case "database":
				this.cfg.setDbEngine(atts.getValue("engine"));
				Database database;
				DbCredentials credentials;
				
				if (cfg.isEnginePostgres()) {
					Class.forName("org.postgresql.Driver");
					database = new PgDatabase(atts.getValue("name"), atts.getValue("schema"));
					credentials = new PgCredentials(atts.getValue("user"), atts.getValue("password"), atts.getValue("host"),atts.getValue("port") != null ? Integer.parseInt(atts.getValue("port")) : 5432, database);
					
				} else if (cfg.isEngineMysql()) {
					database = new MySqlDatabase(atts.getValue("name"));
					credentials = new MySqlCredentials(atts.getValue("user"), atts.getValue("password"), atts.getValue("host"), atts.getValue("port") != null ? Integer.parseInt(atts.getValue("port")) : 3306, database);
					
				} else if (cfg.isEngineFirebird()) {
					Class.forName("org.firebirdsql.jdbc.FBDriver");
					database = new FirebirdDatabase(atts.getValue("name"));
					credentials = new FirebirdCredentials(atts.getValue("user"), atts.getValue("password"), atts.getValue("host"), atts.getValue("file"),atts.getValue("port") != null ? Integer.parseInt(atts.getValue("port")) : 23053, atts.getValue("charSet")  != null ?  atts.getValue("charSet")  : "UTF-8", database);
				} else if (cfg.isEngineSqlite()) {
					Class.forName("org.sqlite.JDBC");
					database = new SqliteDatabase(atts.getValue("name"));
					credentials = new SqliteCredentials(Paths.get(atts.getValue("file")) ,atts.getValue("password") , database);
						
				} else {
					throw new IOException(
							"Database engine \"" + atts.getValue("engine") + "\" is currently not supported");
				}
				cfg.setDatabase(database);
				
				Properties props = credentials.getProperties();
				props.setProperty("charSet",atts.getValue("charset") == null ? "utf8" :atts.getValue("charset"));
				
				// props.setProperty("user", "postgres");
				
				conn = DriverManager.getConnection(credentials.getConnectionUrl(), credentials.getProperties());
				
				break;
			case "output":
				cfg.setBasePath(atts.getValue("basePath"));
				cfg.setModelPath(atts.getValue("modelPath"));
				cfg.setRepositoryPath(atts.getValue("repositoryPath"));
				break;
			case "entities":
				section = Section.ENTITIES;
				break;
			case "entity":
				if (section == Section.ENTITIES) {
					currentEntityTable = cfg.getDatabase().makeTableInstance( atts.getValue("table"));
					cfg.addEntityTable(currentEntityTable);
					cfg.getDatabase().readColumns(currentEntityTable, conn);
					
					if(atts.getValue("overridePrimaryKey")!=null) {
						String[] pkColNames = atts.getValue("overridePrimaryKey").split(",");
						PrimaryKey pk = new PrimaryKey();
						for(String pkCol : pkColNames) {
							Column col = currentEntityTable.getColumnByName(pkCol);
							col.setNullable(false);
							pk.add(col);
						}
						currentEntityTable.setPrimaryKey(pk);
					}
	
					
					if(currentEntityTable.getColumnCount()==0)
						throw new IOException("invalid table " + currentEntityTable.getName());
					cfg.initRelations(currentEntityTable);
				} else {
					throw new SAXException("Illegal state");
				}
				break;
			case "manyToManyRelations":
				section = Section.MANY_TO_MANY_RELATIONS;
				break;
			case "oneToManyRelations":
				section = Section.ONE_TO_MANY_RELATIONS;
				break;
			case "oneRelations":
				section = Section.ONE_RELATIONS;
				break;
			case "mappingTables":
				section = Section.MAPPING_TABLES;
				break;
			case "mapping":
				if (section == Section.MAPPING_TABLES) {
					MappingTable currentMappingTable = new MappingTable(cfg.getDatabase(), atts.getValue("table"), cfg.getDatabase().getDefaultSchema());
					cfg.getDatabase().readColumns(currentMappingTable, conn);	
					if(currentMappingTable.getColumnCount() == 0) {
						throw new IOException("invalid table " + currentMappingTable.getName());
					}
					cfg.addMappingTable(currentMappingTable);
				}
				break;
			case "relation":
				currentSrcTable = cfg.getEntityTable(atts.getValue("src"));
				currentDestTable = cfg.getEntityTable(atts.getValue("dest"));
			
				switch (section) {
				case MANY_TO_MANY_RELATIONS:
					String relQueryNameManyToManyRelation = atts.getValue("queryName");
					currentManyToManyRelation = new ManyRelation(
							relQueryNameManyToManyRelation == null || relQueryNameManyToManyRelation.isEmpty()
									? ("nm" + manyToManyAliasCounter)
									: checkRelationQueryNameUnique(relQueryNameManyToManyRelation));

					cfg.addManyToManyRelation(currentManyToManyRelation, currentSrcTable);
					currentSourceEntityMapping = new M2MColumns();
					currentDestEntityMapping = new M2MColumns();
					currentManyToManyRelation.setSourceMappings(currentSourceEntityMapping);
					currentManyToManyRelation.setDestMappings(currentDestEntityMapping);
					currentManyToManyRelation.setSourceTable(currentSrcTable);
					currentManyToManyRelation.setDestTable(currentDestTable);
					currentMappingTable = cfg.getMappingTable(atts.getValue("mappingTable"));
					currentManyToManyRelation.setMappingTable(currentMappingTable);
					manyToManyAliasCounter++;
					break;
				case ONE_TO_MANY_RELATIONS:
					String relQueryNameOneToManyRelation = atts.getValue("queryName");
					currentOneToManyRelation = new OneToManyRelation(
							relQueryNameOneToManyRelation == null || relQueryNameOneToManyRelation.isEmpty()
									? ("om" + oneToManyAliasCounter)
									: checkRelationQueryNameUnique(relQueryNameOneToManyRelation));

					cfg.addOneToManyRelation(currentOneToManyRelation, currentSrcTable);
					currentOneToManyRelation.setSourceTable(currentSrcTable);
					currentOneToManyRelation.setDestTable(currentDestTable);
					oneToManyAliasCounter++;
					break;
				case ONE_RELATIONS:
					String relQueryNameOneRelation = atts.getValue("queryName");
					currentOneRelation = new OneRelation(
							relQueryNameOneRelation == null || relQueryNameOneRelation.isEmpty()
									? ("o" + oneAliasCounter)
									: checkRelationQueryNameUnique(relQueryNameOneRelation));

					cfg.addOneRelation(currentOneRelation, currentSrcTable);
					currentOneRelation.setSourceTable(currentSrcTable);
					currentOneRelation.setDestTable(currentDestTable);
					oneAliasCounter++;
					break;
				default:
					throw new SAXException("Illegal state");
				}
				
				
				break;
			case "srcColumn":
				switch (section) {
				case MANY_TO_MANY_RELATIONS:
					int i = currentSourceEntityMapping.getColumnCount();
					currentSourceEntityMapping.addColumnMapping(currentSrcTable.getColumnByName(atts.getValue("entityCol")),
					currentMappingTable.getColumnByName(atts.getValue("mappingCol")));
					currentSourceEntityMapping.getEntityColumn(i).setManyToManyRelation(currentManyToManyRelation);
					currentSourceEntityMapping.getMappingColumn(i).setManyToManyRelation(currentManyToManyRelation);

					break;
				default:
					throw new SAXException("Illegal state");
				}
				break;
			case "destColumn":
				switch (section) {
				case MANY_TO_MANY_RELATIONS:
					int i = currentDestEntityMapping.getColumnCount();
					
					currentDestEntityMapping.addColumnMapping(currentDestTable.getColumnByName(atts.getValue("entityCol")),
					currentMappingTable.getColumnByName(atts.getValue("mappingCol")));
					currentDestEntityMapping.getEntityColumn(i).setManyToManyRelation(currentManyToManyRelation);
					currentDestEntityMapping.getMappingColumn(i).setManyToManyRelation(currentManyToManyRelation);
					
					break;
				default:
					throw new SAXException("Illegal state");
				}
				break;
			case "column":
				switch (section) {
				
				case ONE_TO_MANY_RELATIONS:
					Pair<Column, Column> columnMapping = new Pair<Column, Column>(null, null);
					columnMapping.setValue1(currentSrcTable.getColumnByName(atts.getValue("src")));
					Column destTblCol = currentDestTable.getColumnByName(atts.getValue("dest"));
					columnMapping.setValue2(destTblCol);
					destTblCol.setOneToManyRelation(currentOneToManyRelation);
					currentOneToManyRelation.add(columnMapping);
					break;
				case ONE_RELATIONS:
					Pair<Column, Column> columnMappingOneRelation = new Pair<Column, Column>(null, null);
					columnMappingOneRelation.setValue1(currentSrcTable.getColumnByName(atts.getValue("src")));
					Column destTblColOneRelation = currentDestTable.getColumnByName(atts.getValue("dest"));
					columnMappingOneRelation.setValue2(destTblColOneRelation);
					destTblColOneRelation.setOneToManyRelation(currentOneToManyRelation);
					currentOneRelation.add(columnMappingOneRelation);
					break;
				default:
					throw new SAXException("Illegal state");
				}
			default:
				break;
			}
		} catch (Exception e) {
			throw new SAXException(e);
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(tag!=null) {
			switch (tag) {
			case "relation":
				currentSrcTable = null;
				currentDestTable	 = null;	
				break;
	
			default:
				break;
			}
			
			this.tag = null;
		}	
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {

	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub

	}

}
