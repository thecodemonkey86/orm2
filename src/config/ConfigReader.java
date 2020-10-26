package config;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import cpp.entity.SetterValidator;
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
import database.relation.AbstractRelation;
import database.relation.M2MColumns;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.relation.PrimaryKey;
import database.table.MappingTable;
import database.table.Table;
import io.PasswordManager;
import util.Pair;

public class ConfigReader implements ContentHandler {

	protected Connection conn;
	protected OrmConfig cfg;
	
	protected LinkedList<String> tags;
	private enum Section {
		ENTITIES, MAPPING_TABLES, ONE_TO_MANY_RELATIONS, MANY_TO_MANY_RELATIONS, ONE_RELATIONS, OPTIONS
	}

	private Table currentEntityTable;
	private int oneToManyAliasCounter = 1;
	private int manyToManyAliasCounter = 1;
	private int oneAliasCounter = 1;
	private HashMap<String, HashSet<String>> relationQueryNames;
	private OneToManyRelation currentOneToManyRelation;
	private OneRelation currentOneRelation;
	private ManyRelation currentManyToManyRelation;
	private Table currentSrcTable;
	private Table currentDestTable;
	private M2MColumns currentSourceEntityMapping;
	private M2MColumns currentDestEntityMapping;
	private MappingTable currentMappingTable;
	private Section section;
	private Path xmlDirectory;
	private PrimaryKey overrideColsPrimaryKey;
	private String[] enableRawValueColumns;
	
	public OrmConfig getCfg() {
		return cfg;
	}

	public ConfigReader(Path xmlDirectory) {
		relationQueryNames = new HashMap<>();
		this.xmlDirectory = xmlDirectory;
		this.tags = new LinkedList<>();
	}

	protected void createConfig() {
		cfg = new OrmConfig();
	}

	protected String checkRelationQueryNameUnique(String tableName, String name) throws IOException {
		if(!this.relationQueryNames.containsKey(tableName)) {
			this.relationQueryNames.put(tableName, new HashSet<>());
		}
		
		HashSet<String> relationQueryNames = this.relationQueryNames.get(tableName);
		
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
		

	}

	@Override
	public void endDocument() throws SAXException {
		
		if(!cfg.hasBasePath()) {
			cfg.setBasePath(xmlDirectory.toString());
			cfg.setModelPath("model");
			cfg.setRepositoryPath("repository");
		}
		overrideColsPrimaryKey = null;
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
			this.tags.push(localName);
			
			switch (this.tags.peek()) {
			case "orm":
				createConfig();
				break;
			case "database":
				this.cfg.setDbEngine(atts.getValue("engine"));
				Database database=null; 
				DbCredentials credentials;
				
				if(atts.getValue("password") != null) {
					throw new IOException("Remove password from XML");
				}
				
				if (cfg.isEnginePostgres()) {
					Class.forName("org.postgresql.Driver");
					database = new PgDatabase(atts.getValue("name"), atts.getValue("schema"));
					credentials = new PgCredentials(atts.getValue("user"), atts.getValue("host"),atts.getValue("port") != null ? Integer.parseInt(atts.getValue("port")) : 5432, database);
					
				} else if (cfg.isEngineMysql()) {
					database = new MySqlDatabase(atts.getValue("name"));
					credentials = new MySqlCredentials(atts.getValue("user"), atts.getValue("host"), atts.getValue("port") != null ? Integer.parseInt(atts.getValue("port")) : 3306, database);
					
				} else if (cfg.isEngineFirebird()) {
					Class.forName("org.firebirdsql.jdbc.FBDriver");
					database = new FirebirdDatabase(atts.getValue("name"));
					credentials = new FirebirdCredentials(atts.getValue("user"), atts.getValue("host"), atts.getValue("file"),atts.getValue("port") != null ? Integer.parseInt(atts.getValue("port")) : 23053, atts.getValue("charSet")  != null ?  atts.getValue("charSet")  : "UTF-8", database);
				} else if (cfg.isEngineSqlite()) {
					Class.forName("org.sqlite.JDBC");
					database = new SqliteDatabase();
					credentials = new SqliteCredentials(Paths.get(atts.getValue("file")) , database);
						
				} else {
					throw new IOException(
							"Database engine \"" + atts.getValue("engine") + "\" is currently not supported");
				}
				
			
				String password = PasswordManager.loadFromFile(credentials);
				if(password == null && !cfg.isEngineSqlite()) {
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
				
				Properties props = credentials.getProperties();
				props.setProperty("charSet",atts.getValue("charset") == null ? "utf8" :atts.getValue("charset"));
				
				// props.setProperty("user", "postgres");
				
				conn = DriverManager.getConnection(credentials.getConnectionUrl(), credentials.getProperties());
			
				cfg.setDatabase(database);
				cfg.setCredentials(credentials);
			
				
				break;
			case "output":
				String basePath = atts.getValue("basePath");
				if(basePath == null) {
					basePath = xmlDirectory.toString();
				}
				cfg.setBasePath(basePath);
				cfg.setModelPath(atts.getValue("modelPath"));
				cfg.setRepositoryPath(atts.getValue("repositoryPath"));
				cfg.setOverrideRepositoryClassName(atts.getValue("repositoryClassName"));
				break;
			case "entities":
				section = Section.ENTITIES;
				break;
			case "entity":
				if (section == Section.ENTITIES) {
					enableRawValueColumns = null;
					currentEntityTable = cfg.getDatabase().makeTableInstance( atts.getValue("table"));
					cfg.addEntityTable(currentEntityTable);
					currentEntityTable.setOverrideColumnsFromConfig(atts.getValue("overrideColumns")!=null && atts.getValue("overrideColumns").equals("true"));
					if(!currentEntityTable.isOverrideColumnsFromConfig()) {
						overrideColsPrimaryKey = null;
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
					} else {
						overrideColsPrimaryKey = new PrimaryKey();
					}
					if(atts.getValue("overrideNoAutoIncrement")!=null) {
						currentEntityTable.getPrimaryKey().getFirstColumn().setAutoIncrement(false);
					} else if(atts.getValue("autoIncrement")!=null) {
						currentEntityTable.getPrimaryKey().getFirstColumn().setAutoIncrement(atts.getValue("autoIncrement").equals("true"));
					}
					String strQueryTypes = atts.getValue("queryTypes");
					if(strQueryTypes!=null) {
						String[] strArrQueryTypes = strQueryTypes.split(",");
						for (String qt : strArrQueryTypes) {
							switch(qt) {
							case "all":
								currentEntityTable.addQueryType(Table.QueryType.Delete);
								currentEntityTable.addQueryType(Table.QueryType.Update);
								break;							
							case "delete":
								currentEntityTable.addQueryType(Table.QueryType.Delete);
								break;
							case "update":
								currentEntityTable.addQueryType(Table.QueryType.Update);
								break;
							default:
								throw new IOException("valid values are: all,delete,update");
							
							}
						}
						
					} 
					String attEnableRawValue = atts.getValue("enableRawValue");
					if(attEnableRawValue !=null) {
						enableRawValueColumns = attEnableRawValue.split(",");
					}
					
					String attEnableGetValueByName = atts.getValue("enableGetValueByName");
					if(attEnableGetValueByName != null) {
						cfg.setEnableGetValueByName(attEnableGetValueByName.equals("1") ||attEnableGetValueByName.equals("true"));
					}
					
					String attEnableFileImportColumns = atts.getValue("enableFileImportColumns");
					if(attEnableFileImportColumns!=null) {
						String[] fileImportCols =attEnableFileImportColumns.split(",");
						for(String colName : fileImportCols) {
							Column col = currentEntityTable.getColumnByName(colName);
							col.setEnableFileImport(true);
						}
					}
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
			case "relation":
				currentSrcTable = cfg.getEntityTable(atts.getValue("src"));
				currentDestTable = cfg.getEntityTable(atts.getValue("dest"));
				
			
				switch (section) {
				case MANY_TO_MANY_RELATIONS:{
					String attrName = atts.getValue("name");
					String substituteNameSingular;
					String substituteNamePlural;
					if(attrName != null) {
						if(attrName.endsWith("s")) {
							substituteNameSingular = attrName.substring(0,attrName.length()-1);
							substituteNamePlural = attrName;
						} else {
							throw new IOException("name must end with plural-s or be replaced by nameSingular and namePlural");
						}
					} else {
						substituteNameSingular = atts.getValue("nameSingular");
						substituteNamePlural = atts.getValue("namePlural");
					}
					
					String relQueryNameManyToManyRelation = atts.getValue("queryName");
					currentManyToManyRelation = new ManyRelation(
							relQueryNameManyToManyRelation == null || relQueryNameManyToManyRelation.isEmpty()
									? ("nm" + manyToManyAliasCounter)
									: checkRelationQueryNameUnique(currentSrcTable.getName(), relQueryNameManyToManyRelation));

					cfg.addManyToManyRelation(currentManyToManyRelation, currentSrcTable);
					currentSourceEntityMapping = new M2MColumns();
					currentDestEntityMapping = new M2MColumns();
					currentManyToManyRelation.setSourceMappings(currentSourceEntityMapping);
					currentManyToManyRelation.setDestMappings(currentDestEntityMapping);
					currentManyToManyRelation.setSourceTable(currentSrcTable);
					currentManyToManyRelation.setDestTable(currentDestTable);
					
					String mappingTableName = atts.getValue("mappingTable");
					MappingTable mappingTable = new MappingTable(cfg.getDatabase(), mappingTableName, cfg.getDatabase().getDefaultSchema());
					
					String overrideMappingTblPk=atts.getValue("mappingTblOverridePK");
					cfg.getDatabase().readColumns(mappingTable, conn);	
					if(overrideMappingTblPk!=null) {
						PrimaryKey mappingTblPk = new PrimaryKey();
						String[] mappingTblPkParts = overrideMappingTblPk.split(",");
						for(String mappingTblPkCol:mappingTblPkParts) {
							mappingTblPk.add(mappingTable.getColumnByName(mappingTblPkCol));
						}
						
						mappingTable.setPrimaryKey(mappingTblPk);
					}
					if(mappingTable.getColumnCount() == 0) {
						throw new IOException("invalid table " + mappingTable.getName());
					}
					cfg.addMappingTable(mappingTable);
					currentMappingTable = mappingTable;
					currentManyToManyRelation.setMappingTable(mappingTable);
					currentManyToManyRelation.setSubstituteNameSingular(substituteNameSingular);
					currentManyToManyRelation.setSubstituteNamePlural(substituteNamePlural);
					
					String attrSqlOptions=atts.getValue("sqlOptions");
					if(attrSqlOptions != null) {
						String[] sqloptions = attrSqlOptions.split(",");
						for(String s : sqloptions) {
							try {
								currentManyToManyRelation.setSqlOption(AbstractRelation.RelationSqlOptions.valueOf(s));
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
					}
					manyToManyAliasCounter++;
					break;}
				case ONE_TO_MANY_RELATIONS:
				{
					
					String relQueryNameOneToManyRelation = atts.getValue("queryName");
					currentOneToManyRelation = new OneToManyRelation(
							relQueryNameOneToManyRelation == null || relQueryNameOneToManyRelation.isEmpty()
									? ("om" + oneToManyAliasCounter)
									: checkRelationQueryNameUnique(currentSrcTable.getName(), relQueryNameOneToManyRelation));

					cfg.addOneToManyRelation(currentOneToManyRelation, currentSrcTable);
					currentOneToManyRelation.setSourceTable(currentSrcTable);
					currentOneToManyRelation.setDestTable(currentDestTable);
					
					String attrName = atts.getValue("name");
					String substituteNameSingular;
					String substituteNamePlural;
					if(attrName != null) {
						if(attrName.endsWith("s")) {
							substituteNameSingular = attrName.substring(0,attrName.length()-1);
							substituteNamePlural = attrName;
						} else {
							throw new IOException(currentOneToManyRelation.toString()+ ": name must end with plural-s or be replaced by nameSingular and namePlural");
						}
					} else {
						substituteNameSingular = atts.getValue("nameSingular");
						substituteNamePlural = atts.getValue("namePlural");
					}
					currentOneToManyRelation.setSubstituteNameSingular(substituteNameSingular);
					currentOneToManyRelation.setSubstituteNamePlural(substituteNamePlural);
					oneToManyAliasCounter++;
					break;
				}
				case ONE_RELATIONS:
					String relQueryNameOneRelation = atts.getValue("queryName");
					currentOneRelation = new OneRelation(
							relQueryNameOneRelation == null || relQueryNameOneRelation.isEmpty()
									? ("o" + oneAliasCounter)
									: checkRelationQueryNameUnique(currentSrcTable.getName(),relQueryNameOneRelation));

					cfg.addOneRelation(currentOneRelation, currentSrcTable);
					currentOneRelation.setSourceTable(currentSrcTable);
					currentOneRelation.setDestTable(currentDestTable);
					String substituteName = atts.getValue("name");
					currentOneRelation.setSubstituteNameSingular(substituteName);
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
					Column srcColumn = currentSrcTable.getColumnByName(atts.getValue("entityCol"));
					srcColumn.setRelationSourceColumn(true);
					currentSourceEntityMapping.addColumnMapping(srcColumn,
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
					
					Column destColumn = currentDestTable.getColumnByName(atts.getValue("entityCol"));
					destColumn.setRelationDestColumn(true);
					currentDestEntityMapping.addColumnMapping(destColumn,
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
					Column oneToManySrcColumn = currentSrcTable.getColumnByName(atts.getValue("src"));
					oneToManySrcColumn.setRelationSourceColumn(true);
					columnMapping.setValue1(oneToManySrcColumn);
					Column destTblCol = currentDestTable.getColumnByName(atts.getValue("dest"));
					columnMapping.setValue2(destTblCol);
					destTblCol.setOneToManyRelation(currentOneToManyRelation);
					currentOneToManyRelation.add(columnMapping);
					break;
				case ONE_RELATIONS:
					Pair<Column, Column> columnMappingOneRelation = new Pair<Column, Column>(null, null);
					Column oneToOneSrcColumn = currentSrcTable.getColumnByName(atts.getValue("src"));
					oneToOneSrcColumn.setRelationSourceColumn(true);
					columnMappingOneRelation.setValue1(oneToOneSrcColumn);
					Column destTblColOneRelation = currentDestTable.getColumnByName(atts.getValue("dest"));
					destTblColOneRelation.setRelationDestColumn(true);
					columnMappingOneRelation.setValue2(destTblColOneRelation);
					destTblColOneRelation.setOneToManyRelation(currentOneToManyRelation);
					currentOneRelation.add(columnMappingOneRelation);
					break;
				case ENTITIES:
					Column col = cfg.getDatabase().makeColumnInstance(currentEntityTable); 
					col.setName(atts.getValue("name"));
					col.setDbType(atts.getValue("type"));
					String nullable = atts.getValue("nullable") ;
					col.setNullable(nullable != null && (nullable.equals("true")||nullable.equals("1")) );
					if(atts.getValue("primaryKey") != null && atts.getValue("primaryKey").equals("true") ) {
						overrideColsPrimaryKey.add(col);
					}
					String overrideSelect=atts.getValue("overrideSelect");
					if(overrideSelect != null) {
						col.setOverrideSelect(overrideSelect);
					}
					
					String enableRawValue = atts.getValue("enableRawValue");
					
					if(enableRawValue != null) {
						col.setEnableRawValue(enableRawValue.equals("true") || enableRawValue.equals("1"));
					}
					currentEntityTable.addColumn(col);
					break;
				default:
					throw new SAXException("Illegal state");
				}
				break;
			case "options":
				section = Section.OPTIONS;
				break;
			case "renameMethodOnConflict":
				if(section == Section.OPTIONS) {
					cfg.addRenameMethod(atts.getValue("class"),atts.getValue("method"), atts.getValue("newName"));
				}
				break;
			case "validate":
				if (section == Section.ENTITIES) {
					String column= atts.getValue("column");
					String condition= atts.getValue("condition");
					String exceptionMessage = atts.getValue("exceptionMessage");
					String onFail = atts.getValue("onFail");
					
					if(column == null) {
						throw new SAXException("validate column missing");
					}
					if(condition == null) {
						throw new SAXException("validate condition missing");
					}
					
					cfg.addValidators(currentEntityTable.getName(),column,new SetterValidator( condition, 
							 SetterValidator.OnFailValidateMode.fromString(onFail) , exceptionMessage));
					
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SAXException(e);
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(tags.peek()!=null) {
			switch (tags.peek()) {
			case "relation":
				currentSrcTable = null;
				currentDestTable = null;	
				break;
			case "entity":
				if(overrideColsPrimaryKey != null) {
					currentEntityTable.setPrimaryKey(overrideColsPrimaryKey);
				}
				if(enableRawValueColumns!=null) {
					for(String enableRawValueColumn : enableRawValueColumns) {
						currentEntityTable.getColumnByName(enableRawValueColumn.trim()).setEnableRawValue(true);
					}
				}
				break;
			default:
				break;
			}
			
			this.tags.pop();
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
