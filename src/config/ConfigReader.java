package config;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import cpp.entity.SetterValidator;
import database.Database;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.M2MColumns;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.relation.PrimaryKey;
import database.table.MappingTable;
import database.table.Table;
import util.Pair;

public abstract class ConfigReader implements ContentHandler {

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
	private String currentTableUpdatePreconditionOnAllColumns;
	
	public OrmConfig getCfg() {
		return cfg;
	}

	public ConfigReader(Path xmlDirectory,Connection conn, Database database) {
		relationQueryNames = new HashMap<>();
		this.xmlDirectory = xmlDirectory;
		this.tags = new LinkedList<>();
		this.conn = conn;
		createConfig();
		cfg.setDatabase(database);
	}
	
	protected abstract void createConfig() ;


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
			this.tags.push(qName);
			
			switch (this.tags.peek()) {
			case "orm":
				break;
			case "database":
				
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
					currentTableUpdatePreconditionOnAllColumns=atts.getValue("updatePreconditionOnAllColumns");						
					currentEntityTable.setEnableLoadCollection(atts.getValue("enableLoadCollectionMethod")!=null && atts.getValue("enableLoadCollectionMethod").equals("true"));
					if(!currentEntityTable.isOverrideColumnsFromConfig()) {
						overrideColsPrimaryKey = null;
						cfg.getDatabase().readColumns(currentEntityTable, conn,false);
					
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
					if("true".equals(atts.getValue("overrideNotNullAllCols"))) {
						for(Column col:currentEntityTable.getAllColumns()) {
							col.setNullable(false);
						}
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
						currentEntityTable.setEnableGetValueByName(attEnableGetValueByName.equals("1") ||attEnableGetValueByName.equals("true"));
					}
					String enableLoadCollectionMethod = atts.getValue("enableLoadCollectionMethod");
					if(enableLoadCollectionMethod!=null) {
						cfg.setEnableMethodLoadCollection(enableLoadCollectionMethod.equals("1") ||enableLoadCollectionMethod.equals("true") );
					}
					
					String attEnableFileImportColumns = atts.getValue("enableFileImportColumns");
					if(attEnableFileImportColumns!=null) {
						String[] fileImportCols =attEnableFileImportColumns.split(",");
						for(String colName : fileImportCols) {
							Column col = currentEntityTable.getColumnByName(colName);
							col.setEnableFileImport(true);
						}
					}
					String strOptionToManuallyOverrideRelatedTableJoins = atts.getValue("enableOverrideRelatedTableJoins");
					if(strOptionToManuallyOverrideRelatedTableJoins != null) {
						currentEntityTable.setOptionToManuallyOverrideRelatedTableJoins(strOptionToManuallyOverrideRelatedTableJoins.equals("true")||strOptionToManuallyOverrideRelatedTableJoins.equals("1"));
					}
					
					String attEnableMethodHasUpdate= atts.getValue("enableMethodHasUpdate");
					if(attEnableMethodHasUpdate !=null) {
						cfg.enableHasUpdateMethod(currentEntityTable.getUc1stCamelCaseName());
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
					cfg.getDatabase().readColumns(mappingTable, conn,false);	
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
					String attrAdditionalJoin=atts.getValue("additionalJoin");
					if(attrAdditionalJoin!=null) {
						currentManyToManyRelation.setAdditionalJoin(attrAdditionalJoin);
					}
					String attrAdditionalOrderBy=atts.getValue("additionalOrderBy");
					if(attrAdditionalOrderBy!=null) {
						currentManyToManyRelation.setAdditionalOrderBy(attrAdditionalOrderBy);
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
					String attrAdditionalJoin=atts.getValue("additionalJoin");
					if(attrAdditionalJoin!=null) {
						currentOneToManyRelation.setAdditionalJoin(attrAdditionalJoin);
					}
					String attrAdditionalOrderBy=atts.getValue("additionalOrderBy");
					if(attrAdditionalOrderBy!=null) {
						currentOneToManyRelation.setAdditionalOrderBy(attrAdditionalOrderBy);
					}
					String attrComposition=atts.getValue("composition");
					if(attrComposition!=null) {
						currentOneToManyRelation.setComposition(attrComposition.equals("true") || attrComposition.equals("1"));
					}
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
					String attrAdditionalJoin=atts.getValue("additionalJoin");
					if(attrAdditionalJoin!=null) {
						currentOneRelation.setAdditionalJoin(attrAdditionalJoin);
					}
					String attrAdditionalOrderBy=atts.getValue("additionalOrderBy");
					if(attrAdditionalOrderBy!=null) {
						currentOneRelation.setAdditionalOrderBy(attrAdditionalOrderBy);
					}
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
				if(currentEntityTable.isOverrideColumnsFromConfig()) {
					try {
						cfg.getDatabase().readColumns(currentEntityTable, conn,true);
					} catch (SQLException e) {
						e.printStackTrace();
						throw new SAXException(e);
					}
				}
				if(overrideColsPrimaryKey != null) {
					currentEntityTable.setPrimaryKey(overrideColsPrimaryKey);
				}
				if(enableRawValueColumns!=null) {
					for(String enableRawValueColumn : enableRawValueColumns) {
						currentEntityTable.getColumnByName(enableRawValueColumn.trim()).setEnableRawValue(true);
					}
				}
				if(currentTableUpdatePreconditionOnAllColumns != null) {
					for(Column col:currentEntityTable.getAllColumns())
					cfg.addValidators(currentEntityTable.getName(),col.getName(),new SetterValidator( currentTableUpdatePreconditionOnAllColumns, 
							 SetterValidator.OnFailValidateMode.ReturnFalse , null));
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
