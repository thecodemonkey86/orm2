package database.column;

import database.table.AbstractTable;

public class PgColumn extends Column{

	public PgColumn(AbstractTable parentTable) {
		super(parentTable);
	}

	@Override
	public String getEscapedName() {
		return String.format("\"%s\"", getName());
	}

}
