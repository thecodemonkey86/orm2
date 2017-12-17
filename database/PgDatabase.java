package database;

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

public class PgDatabase extends Database {
	protected String defaultSchema;
	
	public PgDatabase(String name, String defaultSchema) {
		super(name);
		this.defaultSchema = defaultSchema;
	}
	

	
	@Override
	public String getEscapedTableName(AbstractTable tbl) {
		return String.format("%s.\"%s\"", defaultSchema,tbl.getName());
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
			Column col = makeColumnInstance();
			col.setName(rsColumndata.getString("column_name"));
			col.setPosition(rsColumndata.getInt("ordinal_position"));
			col.setDbType(rsColumndata.getString("data_type"));
			col.setNullable(rsColumndata.getString("is_nullable").equals("YES"));
			col.setDefaultValue( rsColumndata.getString("column_default"));
			tbl.addColumn(col);
		}
		rsColumndata.close();
		
		if (defaultSchema.equals("pg_catalog")) {
			Column col = makeColumnInstance();
			col.setName("oid");
			col.setPosition(0);
			col.setDbType("integer");
			col.setNullable(false);
			tbl.addColumn(col);
		}
		
		PrimaryKey primaryKey = new PrimaryKey();
		
		String sql=String.format("SELECT a.attname as colname , exists(select s.relname as seq, n.nspname as sch, t.relname as tab, a.attname as col from pg_class s  join pg_depend d on d.objid=s.oid and d.classid='pg_class'::regclass and d.refclassid='pg_class'::regclass  join pg_class t on t.oid=d.refobjid  join pg_namespace n on n.oid=t.relnamespace  join pg_attribute a0 on a0.attrelid=t.oid and a0.attnum=d.refobjsubid where s.relkind='S' and d.deptype='a'and n.nspname = '%s' and a.attname = a0.attname )  as autoincrement FROM   pg_index i JOIN   pg_attribute a ON a.attrelid = i.indrelid AND a.attnum = ANY(i.indkey) WHERE  i.indrelid = '%s.%s'::regclass AND    i.indisprimary;", tbl.getSchema(), tbl.getSchema(), tbl.getName());
		Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY  );
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()) {
			Column c = tbl.getColumnByName(rs.getString("colname"));
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
	public String getDefaultSchema() {
		return "public";
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
	public String sqlInsertMultiRow(AbstractTable tbl, List<String> columns, String placeholders) {
		// TODO Auto-generated method stub
		return null;
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
	public Column makeColumnInstance() {
		return new PgColumn();
	}

	

}
