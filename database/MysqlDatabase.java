package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import database.column.Column;
import database.relation.PrimaryKey;
import database.table.AbstractTable;

public class MysqlDatabase extends Database{

	public MysqlDatabase(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	

	@Override
	public char getColumnEscapeChar() {
		return '`';
	}

	

	@Override
	public void readAllColumns(AbstractTable tbl, Connection conn) throws SQLException {
		if (stColumndata==null)
			stColumndata = conn.prepareStatement("select * from information_schema.columns where table_name = ? and TABLE_SCHEMA = ?");
		stColumndata.setString(1, tbl.getName());
		stColumndata.setString(2, getName());
		ResultSet rsColumndata = stColumndata.executeQuery();
		while (rsColumndata.next()) {
			Column col = new Column();
			col.setName(rsColumndata.getString("column_name"));
			col.setPosition(rsColumndata.getInt("ordinal_position"));
			col.setDbType(rsColumndata.getString("data_type"));
			col.setNullable(rsColumndata.getString("is_nullable").equals("YES"));
			col.setDefaultValue( rsColumndata.getString("column_default"));
			tbl.addColumn(col);
		}
		rsColumndata.close();
		
	}

	

	@Override
	public String sqlInsertOrUpdate(AbstractTable tbl, List<String> columns
			) {
		throw new RuntimeException("not implemented");
	}
	@Override
	public String sqlInsertOrIgnoreMultiRow(AbstractTable tbl, List<String> columns, String placeholders
			) {
		throw new RuntimeException("not implemented");
	}

	@Override
	public String getBooleanExpressionTrue() {
		return "1";
	}



	@Override
	public void readPrimaryKey(AbstractTable table, Connection conn) throws SQLException {
		if (stPkColumndata==null)
			stPkColumndata = conn.prepareStatement("select * from information_schema.columns where table_name = ? and TABLE_SCHEMA = ? and column_key = ?");
		stPkColumndata.setString(1, table.getName());
		stPkColumndata.setString(2, getName());
		stPkColumndata.setString(3, "PRI");
		ResultSet rsColumndata = stPkColumndata.executeQuery();
		PrimaryKey pk = new PrimaryKey();
		while (rsColumndata.next()) {
			Column col = table.getColumnByName(rsColumndata.getString("column_name"));
			col.setAutoIncrement(rsColumndata.getString("extra").equals("auto_increment"));
			pk.add(col);
		}
		table.setPrimaryKey(pk);
		rsColumndata.close();
		
	}



	@Override
	public String getDefaultSchema() {
		return null;
	}



	@Override
	public String sqlInsert(AbstractTable tbl, List<String> columns) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public boolean supportsInsertOrIgnore() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public String sqlInsertMultiRow(AbstractTable tbl, List<String> columns, String placeholders) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public boolean supportsMultiRowInsert() {
		// TODO Auto-generated method stub
		return false;
	}

//	@Override
//	public String getDefaultValue(String string) {
//		throw new RuntimeException("not impl");
//	}

}
