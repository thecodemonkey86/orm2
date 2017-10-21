package database.table;

import database.Database;

public class MappingTable extends AbstractTable{

	public MappingTable(Database db,String name,String schema) {
		super(db,name,schema);
	}

	public int getColumnCount() {
		return allColumns.size();
	}
	
	
}
