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
		return name;
	}
	
	

	
	public abstract String getEscapedTableName(AbstractTable tbl) ;
	
	
	//public abstract void readPrimaryKey(AbstractTable table,Connection conn) throws SQLException;
	public abstract void readColumns(AbstractTable tbl,Connection conn) throws SQLException;
	
	public abstract String sqlInsertOrUpdate(AbstractTable tbl, List<String> matchingColumns);
	public String sqlInsert(AbstractTable tbl) {
		return getDefaultSqlInsert(tbl);
	}
	public abstract String sqlInsertOrIgnoreMultiRow(AbstractTable tbl,  String placeholders);
	public abstract String sqlInsertMultiRow(AbstractTable tbl, String placeholders);
	public abstract String getBooleanExpressionTrue();

	public abstract String getDefaultSchema() ;

	public abstract boolean supportsInsertOrIgnore();
	public abstract boolean supportsMultiRowInsert();

	public Table makeTableInstance( String name) {
		return new Table(this, name, null);
	}
	
	public abstract Column makeColumnInstance();

	protected String getDefaultSqlInsert(AbstractTable tbl) {
		ArrayList<String> colNames = new ArrayList<>(); 
		for(Column c:tbl.getAllColumns()) {
			colNames.add(c.getEscapedName());
		}
		return String.format("insert into %s (%s) values (%s)",tbl.getEscapedName(),CodeUtil.commaSep(colNames),CodeUtil2.strMultiply("?", ",", colNames.size()));
	}
}
