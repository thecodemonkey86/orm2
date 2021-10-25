package database.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import database.Database;
import database.column.Column;
import database.relation.ForeignKey;
import util.CodeUtil2;


public class Table extends AbstractTable{
	protected int oid;
	
	protected ArrayList<ForeignKey> foreignKeys;
	public static enum QueryType{Update, Delete};
	protected boolean optionToManuallyOverrideRelatedTableJoins;
	protected Set<QueryType> queryTypes;


	public boolean addForeignKey(ForeignKey e) {
		return foreignKeys.add(e);
	}

	public boolean getOptionToManuallyOverrideRelatedTableJoins() {
		return optionToManuallyOverrideRelatedTableJoins;
	}
	public void setOptionToManuallyOverrideRelatedTableJoins(boolean optionToManuallyOverrideRelatedTableJoins) {
		this.optionToManuallyOverrideRelatedTableJoins = optionToManuallyOverrideRelatedTableJoins;
	}
	
	
	public List<Column> getFieldColumns() {
		ArrayList<Column> res=new ArrayList<>();
		for(Column col:allColumns) {
			if(!col.isPartOfPk() && !col.isRelationSourceColumn()) {
				res.add(col);
			}
		}
		return res;
	}
	
	public List<Column> getColumnsWithoutPrimaryKey() {
		ArrayList<Column> res=new ArrayList<>();
		for(Column col:allColumns) {
			if(!col.isPartOfPk()) {
				res.add(col);
			}
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public List<Column> getColumns(boolean includeAutoIncrementColumn) {
		
		if (includeAutoIncrementColumn) {
			return (List<Column>) allColumns.clone();
		}
		ArrayList<Column> res=new ArrayList<>();
		for(Column col:allColumns) {
			if(!col.isAutoIncrement()) {
				res.add(col);
			}
		}
		return res;
	}
	
	public ArrayList<ForeignKey> getForeignKeys() {
		return foreignKeys;
	}
	

	
	public Table(Database db, String name, String schema) {
		super(db,name,schema);
		foreignKeys = new ArrayList<>();
	}
	
	
	public void setOid(int oid) {
		this.oid = oid;
	}
	
	public int getOid() {
		return oid;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getUc1stCamelCaseName() {
		return CodeUtil2.uc1stCamelCase(getName());
	}
	
	public String getCamelCaseName() {
		return CodeUtil2.camelCase(getName());
	}

	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Table) {
			return ((Table)obj).name.equals(this.name);
		}
		return false;
	}




	public boolean isAutoIncrement() {
		return primaryKey.isAutoIncrement();
	}


	public void addQueryType(QueryType queryType) {
		if(this.queryTypes == null) {
			this.queryTypes = new HashSet<>();
		}
		this.queryTypes.add(queryType);
	}
	
	public boolean hasQueryType(QueryType queryType) {
		if(this.queryTypes == null) {
			return false;
		}
		return queryTypes.contains(queryType);
	}


	
}
