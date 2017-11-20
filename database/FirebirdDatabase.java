package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import database.column.Column;
import database.column.FirebirdColumn;
import database.relation.PrimaryKey;
import database.table.AbstractTable;
import database.table.FirebirdTable;
import database.table.Table;

public class FirebirdDatabase extends Database {

	public FirebirdDatabase(String name) {
		super(name);
	}


	

	@Override
	public void readColumns(AbstractTable table, Connection conn) throws SQLException {
		String sql = "SELECT rf.rdb$field_name as field_name, rf.rdb$field_position as field_position, f.rdb$field_type as field_type, rf.rdb$null_flag as null_flag, f.rdb$default_value as default_value from rdb$fields f join RDB$RELATION_FIELDS rf on rf.RDB$FIELD_SOURCE = f.RDB$FIELD_NAME where RDB$RELATION_NAME = ?"; 
		if (stColumndata==null)
			stColumndata = conn.prepareStatement(sql);
		
		stColumndata.setString(1, table.getName().toUpperCase());
		ResultSet rsColumndata = stColumndata.executeQuery();
		
		while (rsColumndata.next()) {
			Column col = new FirebirdColumn();
			col.setName(rsColumndata.getString("field_name").trim().toLowerCase());
			col.setPosition(rsColumndata.getInt("field_position"));
			col.setDbType(rsColumndata.getString("field_type"));
			col.setNullable(rsColumndata.getInt("null_flag")!=1);
			col.setDefaultValue( rsColumndata.getString("default_value"));
			table.addColumn(col);
		}
		rsColumndata.close();
		
		sql = "select sg.rdb$field_name as field_name, sg.rdb$field_position as field_position,f.rdb$field_type as field_type , rf.rdb$null_flag as null_flag, f.rdb$default_value as default_value from     rdb$indices ix     left join rdb$index_segments sg on ix.rdb$index_name = sg.rdb$index_name     left join rdb$relation_constraints rc on rc.rdb$index_name = ix.rdb$index_name join rdb$relation_fields rf on rf.rdb$relation_name = rc.rdb$relation_name and rf.rdb$field_name = sg.rdb$field_name join RDB$FIELDS f on rf.RDB$FIELD_SOURCE = f.RDB$FIELD_NAME where     rc.rdb$constraint_type = 'PRIMARY KEY' and rc.rdb$relation_name = ?"; 
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, table.getName().toUpperCase());
		rsColumndata = stmt.executeQuery();
		PrimaryKey primaryKey = new PrimaryKey();
		while (rsColumndata.next()) {
			Column col = new FirebirdColumn();
			col.setName(rsColumndata.getString("field_name").trim().toLowerCase());
			col.setPosition(rsColumndata.getInt("field_position"));
			col.setDbType(rsColumndata.getString("field_type"));
			col.setNullable(rsColumndata.getInt("null_flag")!=1);
			col.setDefaultValue( rsColumndata.getString("default_value"));
			primaryKey.add(col);
		}
		table.setPrimaryKey(primaryKey);
		rsColumndata.close();
	}

	@Override
	public String sqlInsertOrUpdate(AbstractTable tbl, List<String> columns) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sqlInsertOrIgnoreMultiRow(AbstractTable tbl, String placeholders) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBooleanExpressionTrue() {
		return "true";
	}

	@Override
	public String getDefaultSchema() {
		return null;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (stColumndata!=null)
			stColumndata.close();
	}

	@Override
	public boolean supportsInsertOrIgnore() {
		return false;
	}

	@Override
	public String sqlInsert(AbstractTable tbl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sqlInsertMultiRow(AbstractTable tbl, List<String> columns, String placeholders) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supportsMultiRowInsert() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public Table makeTableInstance(String value) {
		return new FirebirdTable(this, value);
	}
	
	@Override
	public String getEscapedTableName(AbstractTable tbl) {
		return String.format("\"%s\"", tbl.getName().toUpperCase());
	}


	@Override
	public Column makeColumnInstance() {
		return new FirebirdColumn();
	}
}
