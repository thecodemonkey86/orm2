package database.relation;

import java.util.ArrayList;
import java.util.List;

import database.column.Column;
import util.Pair;

//public class M2MColumns extends Pair<Column, Column>{
//
//	public M2MColumns(Column value1, Column value2) {
//		super(value1, value2);
//	}
//	
//}
public class M2MColumns {

	private List<Pair<Column,Column>> entityToMappingColumns;
	
	public M2MColumns() {
		entityToMappingColumns = new ArrayList<>();
	}
	
	public int getColumnCount() {
		return entityToMappingColumns.size();
	}
	
	public void addColumnMapping(Column entityColumn, Column mappingColumn) {
		this.entityToMappingColumns.add(new Pair<Column, Column>(entityColumn, mappingColumn));
	}
	
	public Column getEntityColumn(int index) {
		return entityToMappingColumns.get(index).getValue1();
	}
	
	public Column getMappingColumn(int index) {
		return entityToMappingColumns.get(index).getValue2();
	}
}
