package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import database.column.Column;
import database.table.AbstractTable;
import database.table.Table;


public abstract class Database {
	
	
	
	String name;
	protected PreparedStatement stColumndata = null;
	protected PreparedStatement stPkColumndata = null;
	
	public Database(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	

	
	public abstract String getEscapedTableName(AbstractTable tbl) ;
	
	
	//public abstract void readPrimaryKey(AbstractTable table,Connection conn) throws SQLException;
	public abstract void readColumns(AbstractTable tbl,Connection conn) throws SQLException;
	
	public abstract String sqlInsertOrUpdate(AbstractTable tbl, List<String> matchingColumns);
	public abstract String sqlInsert(AbstractTable tbl);
	public abstract String sqlInsertOrIgnoreMultiRow(AbstractTable tbl,  String placeholders);
	public abstract String sqlInsertMultiRow(AbstractTable tbl, List<String> columns, String placeholders);
	public abstract String getBooleanExpressionTrue();

	public abstract String getDefaultSchema() ;

	public abstract boolean supportsInsertOrIgnore();
	public abstract boolean supportsMultiRowInsert();

	public Table makeTableInstance( String name) {
		return new Table(this, name, null);
	}
	
	public abstract Column makeColumnInstance();
	
}
