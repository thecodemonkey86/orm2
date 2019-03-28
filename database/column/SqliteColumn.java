package database.column;

import database.table.AbstractTable;

public class SqliteColumn extends Column{

	public SqliteColumn(AbstractTable parentTable) {
		super(parentTable);
	}

	@Override
	public String getEscapedName() {
		return String.format("\"%s\"", getName());
	}

}
