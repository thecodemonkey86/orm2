package database.relation;

import java.util.ArrayList;

import database.column.Column;
import database.table.Table;
import util.Pair;

public class ForeignKey {
	protected Table localTable,foreignTable;
	protected ArrayList<Pair<Column,Column>> localToForeignColumns;
	
	
	public ForeignKey() {
		localToForeignColumns = new ArrayList<>();
	}
	public Pair<Column, Column> get(int index) {
		return localToForeignColumns.get(index);
	}

	public void addColumnPair(Pair<Column,Column>  pair) {
		this.localToForeignColumns.add(pair);
	}
	
	public Table getForeignTable() {
		return foreignTable;
	}
	
	public Table getLocalTable() {
		return localTable;
	}
	
	public int getColumnCount() {
		return localToForeignColumns.size();
	}
	
	
}
