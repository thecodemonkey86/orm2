package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.column.Column;
import database.column.MySqlColumn;
import database.relation.PrimaryKey;
import database.table.AbstractTable;
import util.CodeUtil2;

public class MySqlDatabase extends Database {
	protected String dbUser, dbPass, dbUrl;

	public MySqlDatabase(String name) throws IOException {
		super(name);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void readColumns(AbstractTable tbl, Connection conn,boolean columnsFromConfig) throws SQLException {
		if (stColumndata == null)
			stColumndata = conn.prepareStatement(
					"select column_name,ordinal_position,data_type,is_nullable,column_default, IF(COLUMN_TYPE LIKE '%unsigned', 'YES', 'NO') as IS_UNSIGNED  from information_schema.columns where table_name = ? and TABLE_SCHEMA = ?");
		stColumndata.setString(1, tbl.getName());
		stColumndata.setString(2, getName());
		ResultSet rsColumndata = stColumndata.executeQuery();
		while (rsColumndata.next()) {
			Column col = makeColumnInstance(tbl);
			col.setName(rsColumndata.getString("column_name"));
			col.setPosition(rsColumndata.getInt("ordinal_position"));
			col.setDbType(fixUnsigned(rsColumndata.getString("data_type"),rsColumndata.getString("IS_UNSIGNED").equals("YES")));
			col.setNullable(rsColumndata.getString("is_nullable").equals("YES"));
			col.setDefaultValue(rsColumndata.getString("column_default"));
			tbl.addColumn(col);
		}
		rsColumndata.close();

		
		if (stPkColumndata == null)
			stPkColumndata = conn.prepareStatement(
					"select column_name,extra,column_type,IF(COLUMN_TYPE LIKE '%unsigned', 'YES', 'NO') as IS_UNSIGNED from information_schema.columns where table_name = ? and TABLE_SCHEMA = ? and column_key = ?");
		stPkColumndata.setString(1, tbl.getName());
		stPkColumndata.setString(2, getName());
		stPkColumndata.setString(3, "PRI");
		rsColumndata = stPkColumndata.executeQuery();
		PrimaryKey pk = new PrimaryKey();
		while (rsColumndata.next()) {
			Column col = tbl.getColumnByName(rsColumndata.getString("column_name"));
			col.setNullable(false);
			col.setAutoIncrement(rsColumndata.getString("extra").equals("auto_increment"));
			col.setDbType(fixUnsigned(col.getDbType(),rsColumndata.getString("IS_UNSIGNED").equals("YES")));
			pk.add(col);
		}
		tbl.setPrimaryKey(pk);
		rsColumndata.close();
	}

	private static String fixUnsigned(String type, boolean unsigned) {
		if(unsigned) {
			if(!type.endsWith("_unsigned")) {
				return type+"_unsigned";
			}
		}
		if(type.endsWith("unsiqned_unsigned")) {
			throw new RuntimeException(type);
		}
		return type;
	}


	@Override
	public String sqlInsertOrUpdate(AbstractTable tbl, List<String> columns) {
		throw new RuntimeException("not implemented");
	}

	@Override
	public String sqlInsertOrIgnoreMultiRow(AbstractTable tbl, String placeholders) {
		throw new RuntimeException("not implemented");
	}

	@Override
	public String getBooleanExpressionTrue() {
		return "1";
	}

	

	@Override
	public String getDefaultSchema() {
		return null;
	}


	@Override
	public boolean supportsInsertOrIgnore() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String sqlInsertMultiRow(AbstractTable tbl, String placeholders) {
		ArrayList<String> columnsNamesEscaped = new ArrayList<>(); 
		for(Column c:tbl.getAllColumns()) {
			columnsNamesEscaped.add(c.getEscapedName());
		}	
		return "insert into " +getEscapedTableName(tbl) + " " + CodeUtil2.parentheses( CodeUtil2.concat(columnsNamesEscaped, ",")) + " values "+placeholders
				;
	}

	@Override
	public boolean supportsMultiRowInsert() {
		return true;
	}

	@Override
	public String getEscapedTableName(AbstractTable tbl) {
		return String.format("`%s`", tbl.getName());
	}


	@Override
	public Column makeColumnInstance(AbstractTable tbl) {
		return new MySqlColumn(tbl);
	}


	@Override
	public String sqlInsertMultiRow(AbstractTable tbl, List<Column> columnsInSpecificOrder, String placeholders) {
		throw new RuntimeException("not implemented");
	}


	

}
