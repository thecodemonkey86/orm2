package database.table;

import database.Database;

public class MappingTable extends AbstractTable{
	public MappingTable(Database db,String name) {
		super(db,name,null);
	}
	public MappingTable(Database db,String name,String schema) {
		super(db,name,schema);
	}

	
	
	
}
