package database.column;

import database.table.AbstractTable;

public class MySqlColumn extends Column{

	public MySqlColumn(AbstractTable parentTable) {
		super(parentTable);
	}

	@Override
	public String getEscapedName() {
		return String.format("`%s`", getName());
	}

}
