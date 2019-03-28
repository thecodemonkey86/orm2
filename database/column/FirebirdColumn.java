package database.column;

import database.table.AbstractTable;

public class FirebirdColumn extends Column {
	
	
	public FirebirdColumn(AbstractTable parentTable) {
		super(parentTable);
	}

	@Override
	public String getEscapedName() {
		//return String.format("\"%s\"", getName().toUpperCase());
		return getName().toUpperCase();
	}
}
