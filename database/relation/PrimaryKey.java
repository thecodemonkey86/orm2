package database.relation;

import java.util.ArrayList;
import java.util.Iterator;

import database.column.Column;

public class PrimaryKey implements Iterable<Column>{
	protected ArrayList<Column> columns;
	
	public PrimaryKey() {
		columns = new ArrayList<>();
	}
	
	public boolean add(Column e) {
		return columns.add(e);
	}
	public ArrayList<Column> getColumns() {
		return columns;
	}
	
	
	public Column getFirstColumn() {
		return columns.get(0);
	}
	
	public boolean isAutoIncrement() {
		return columns.get(0).isAutoIncrement();
	}
	
	public Column getAutoIncrementColumn() {
		if (columns.get(0).isAutoIncrement()) 
			return columns.get(0); 
		else 
			return null;
	}

	public boolean isMultiColumn() {
		return columns.size()>1;
	}

	public int getColumnCount() {
		return columns.size();
	}

	public Column getColumn(int i) {
		return columns.get(i);
	}
	
	@Override
	public String toString() {
		return columns.toString();
	}

	@Override
	public Iterator<Column> iterator() {
		return columns.iterator();
	}
	
}
