package database.table;

import database.Database;
import generate.CodeUtil2;

public class FirebirdTable extends Table {

	public FirebirdTable(Database db, String name) {
		super(db, name, null);
	}

	public String getUc1stCamelCaseName() {
		return CodeUtil2.uc1stCamelCase(getName().toLowerCase());
	}
	
	public String getCamelCaseName() {
		return CodeUtil2.camelCase(getName().toLowerCase());
	}
	
}
