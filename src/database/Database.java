package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import database.column.Column;
import database.table.AbstractTable;
import database.table.Table;
import util.CodeUtil2;


public abstract class Database {
	
	
	
	String name;
	protected PreparedStatement stColumndata = null;
	protected PreparedStatement stPkColumndata = null;
	
	public Database(String name) {
		this.name = name;
	}
	
	public String getName() {
		if(name == null) {
			throw new RuntimeException("Database name missing");
		}
		return name;
	}
	
	

	
	public abstract String getEscapedTableName(AbstractTable tbl) ;
	public abstract void readColumns(AbstractTable tbl,Connection conn) throws SQLException;
	
	public abstract String sqlInsertOrUpdate(AbstractTable tbl, List<String> matchingColumns);
	public String sqlInsert(AbstractTable tbl) {
		return getDefaultSqlInsert(tbl);
	}
	public String sqlDelete(AbstractTable tbl, String condition) {
		return String.format("DELETE FROM %s WHERE %s",tbl.getEscapedName(), condition);
	}
	
	public abstract String sqlInsertOrIgnoreMultiRow(AbstractTable tbl,  String placeholders);
	public abstract String sqlInsertMultiRow(AbstractTable tbl, String placeholders);
	public abstract String sqlInsertMultiRow(AbstractTable tbl, List<Column> columnsInSpecificOrder, String placeholders);
	public abstract String getBooleanExpressionTrue();

	public abstract String getDefaultSchema() ;

	public abstract boolean supportsInsertOrIgnore();
	public abstract boolean supportsMultiRowInsert();

	
	
	public Table makeTableInstance( String name) {
		return new Table(this, name, null);
	}
	
	public abstract Column makeColumnInstance(AbstractTable parentTable);

	protected String getDefaultSqlInsert(AbstractTable tbl) {
		ArrayList<String> colNames = new ArrayList<>(); 
		for(Column c:tbl.getAllColumns()) {
			colNames.add(c.getEscapedName());
		}
		return String.format("insert into %s (%s) values (%s)",tbl.getEscapedName(),CodeUtil.commaSep(colNames),CodeUtil2.strMultiply("?", ",", colNames.size()));
	}
}
