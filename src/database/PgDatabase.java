package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.column.Column;
import database.column.PgColumn;
import database.relation.PrimaryKey;
import database.table.AbstractTable;
import database.table.Table;
import util.CodeUtil2;
import util.StringUtil;

public class PgDatabase extends Database {
	protected String defaultSchema;
	
	public PgDatabase(String name, String defaultSchema) throws IOException {
		super(name);
		this.defaultSchema = defaultSchema != null ? defaultSchema : "public";
	}
	

	
	@Override
	public String getEscapedTableName(AbstractTable tbl) {
		return String.format("\"%s\".\"%s\"", defaultSchema,tbl.getName());
	}

	@Override
	public void readColumns(AbstractTable tbl, Connection conn)
			throws SQLException {
		if (stColumndata==null)
			stColumndata = conn.prepareStatement("select * from information_schema.columns where table_name = ? and TABLE_CATALOG = ?");
		stColumndata.setString(1, tbl.getName());
		stColumndata.setString(2, getName());
		ResultSet rsColumndata = stColumndata.executeQuery();
		while (rsColumndata.next()) {
			Column col = makeColumnInstance(tbl);
			col.setName(rsColumndata.getString("column_name"));
			col.setPosition(rsColumndata.getInt("ordinal_position"));
			col.setDbType(rsColumndata.getString("data_type"));
			col.setNullable(rsColumndata.getString("is_nullable").equals("YES"));
			String defaultValue = rsColumndata.getString("column_default");
			if(defaultValue != null && defaultValue.contains("::")) {
				defaultValue = StringUtil.dropAll( defaultValue.split("::")[0], '\'');
			}
			col.setDefaultValue(defaultValue );
			tbl.addColumn(col);
		}
		rsColumndata.close();
		
		if (defaultSchema.equals("pg_catalog")) {
			Column col = makeColumnInstance(tbl);
			col.setName("oid");
			col.setPosition(0);
			col.setDbType("integer");
			col.setNullable(false);
			tbl.addColumn(col);
		}
		
		PrimaryKey primaryKey = new PrimaryKey();
		
		String sql=String.format("SELECT a.attname as colname , exists(select *\r\n" + 
				"from information_schema.columns col\r\n" + 
				"where col.column_default is not null\r\n" + 
				"      and col.table_schema not in('information_schema', 'pg_catalog')\r\n" + 
				"and table_schema='%s' and table_name='%s' and column_name=a.attname and column_default like 'nextval(%%')  as autoincrement FROM   pg_index i JOIN   pg_attribute a ON a.attrelid = i.indrelid AND a.attnum = ANY(i.indkey) WHERE  i.indrelid = '%s.%s'::regclass AND    i.indisprimary;", tbl.getSchema(),tbl.getName(), tbl.getSchema(), tbl.getName());
		Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY  );
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()) {
			Column c = tbl.getColumnByName(rs.getString("colname"));
			c.setNullable(false);
			c.setAutoIncrement(rs.getBoolean("autoincrement"));
			primaryKey.add(c);
		}
		tbl.setPrimaryKey(primaryKey);
	}

	

	@Override
	public String sqlInsertOrUpdate(AbstractTable tbl, List<String> columns) {
		throw new RuntimeException("not implemented");
	}
	
	@Override
	public String sqlInsertOrIgnoreMultiRow(AbstractTable tbl,String placeholders
			) {
		
		ArrayList<String> pkColNames = new ArrayList<>(); 
		for(Column c:tbl.getPrimaryKey().getColumns()) {
			pkColNames.add(c.getEscapedName());
		}
		ArrayList<String> columns = new ArrayList<>(); 
		for(Column c:tbl.getAllColumns()) {
			columns.add(c.getEscapedName());
		}	
		return "insert into " +getEscapedTableName(tbl) + " " + CodeUtil2.parentheses( CodeUtil2.concat(columns, ",")) + " values "+placeholders
				+ " on conflict "+CodeUtil2.parentheses(CodeUtil2.concat(pkColNames,"," ))
				+" do nothing"
				;
	}

	@Override
	public String getBooleanExpressionTrue() {
		return "true";
	}




	@Override
	public boolean supportsInsertOrIgnore() {
		return true;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (stColumndata!=null)
			stColumndata.close();
	}
//	@Override
//	public String getDefaultValue(String string) {
//		if (string == null||string.startsWith("nextval(")) {
//			return null;
//		}
//		return (string.isEmpty()) ? null : string;
//	}



	@Override
	public String sqlInsert(AbstractTable tbl) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String sqlInsertMultiRow(AbstractTable tbl,  String placeholders) {
		ArrayList<String> pkColNames = new ArrayList<>(); 
		for(Column c:tbl.getPrimaryKey().getColumns()) {
			pkColNames.add(c.getEscapedName());
		}
		ArrayList<String> columns = new ArrayList<>(); 
		for(Column c:tbl.getAllColumns()) {
			columns.add(c.getEscapedName());
		}	
		return "insert into " +getEscapedTableName(tbl) + " " + CodeUtil2.parentheses( CodeUtil2.concat(columns, ",")) + " values "+placeholders
				 
				;
	}

	@Override
	public String getDefaultSchema() {
		return defaultSchema;
	}


	@Override
	public boolean supportsMultiRowInsert() {
		return true;
	}
	
	@Override
	public Table makeTableInstance(String value) {
		return new Table(this, value, defaultSchema);
	}



	@Override
	public Column makeColumnInstance(AbstractTable tbl) {
		return new PgColumn(tbl);
	}



	@Override
	public String sqlInsertMultiRow(AbstractTable tbl, List<Column> columnsInSpecificOrder, String placeholders) {
		ArrayList<String> pkColNames = new ArrayList<>(); 
		for(Column c:tbl.getPrimaryKey().getColumns()) {
			pkColNames.add(c.getEscapedName());
		}
		ArrayList<String> columns = new ArrayList<>(); 
		for(Column c:columnsInSpecificOrder) {
			columns.add(c.getEscapedName());
		}	
		return "insert into " +getEscapedTableName(tbl) + " " + CodeUtil2.parentheses( CodeUtil2.concat(columns, ",")) + " values "+placeholders
				 
				;
	}

	@Override
	public boolean supportsLoadingFiles() {
		return true;
	}

	@Override
	public String getFileLoadFunction() {
		return "bytea_import";
	}
}
