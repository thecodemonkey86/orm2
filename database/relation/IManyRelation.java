package database.relation;

import database.column.Column;
import database.table.Table;

public interface IManyRelation {

	int getDestColumnCount();

	Column getDestMappingColumn(int i);

	Table getDestTable();

	Table getSourceTable();

	String getAlias();


}
