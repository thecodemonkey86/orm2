package database.table;

import java.util.ArrayList;

import database.Database;
import database.column.Column;
import database.relation.PrimaryKey;


public abstract class AbstractTable {
	protected String schema;
	protected String name;
	protected ArrayList<Column> allColumns;
	protected PrimaryKey primaryKey;
	protected Database db;
	
	public void setPrimaryKey(PrimaryKey primaryKey) {
		if (primaryKey.getColumns().size() == 0) {
			throw new IllegalArgumentException("pk columns missing: " + name);
		}
		this.primaryKey = primaryKey;
	}
	
	public PrimaryKey getPrimaryKey() {
		if(primaryKey==null) {
			throw new NullPointerException();
		}
		return primaryKey;
	}
	
	
	public AbstractTable(Database db, String name, String schema) {
		allColumns = new ArrayList<>();
		this.db = db;
		this.schema = schema;
		this.name = name;
	}
	
	public boolean addColumn(Column e) {
		return allColumns.add(e);
	}
	
	public ArrayList<Column> getAllColumns() {
		return allColumns;
	}

	public String getName() {
		return name;
	}
	
	public Column getColumnByName(String name) {
		for(Column c:allColumns) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
			
		throw new RuntimeException("no such column "+name+ " in table "+getName());
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getEscapedName() {
		return db.getEscapedTableName(this);
	}
	
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	public String getSchema() {
		return schema;
	}
	
	public int getColumnCount() {
		return allColumns.size();
	}
}
