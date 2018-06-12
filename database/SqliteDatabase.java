package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import codegen.CodeUtil;
import database.column.Column;
import database.column.SqliteColumn;
import database.relation.PrimaryKey;
import database.table.AbstractTable;
import util.CodeUtil2;
import util.StringUtil;

public class SqliteDatabase extends Database {
	
	public SqliteDatabase() throws IOException {
		super(null);
	}

	private static List<String> getTokenList(String sql) {
		List<String> l = new ArrayList<String>();

		int lastPos = -1;
		for(int i=0;i<sql.length();i++) {
			if(sql.charAt(i) == '\r'
					|| sql.charAt(i) == '\t' 
					|| sql.charAt(i) == ' ' 
					||sql.charAt(i) == '\n' 
					|| sql.charAt(i) == '(' 
					|| sql.charAt(i) == ')'
					|| sql.charAt(i) == ',') {
				String token = sql.substring(lastPos + 1, i);
				if(!token.trim().isEmpty()) {
					l.add(token.trim());
				}
				if( sql.charAt(i) == '(' 
						|| sql.charAt(i) == ')'
						|| sql.charAt(i) == ',') {
							l.add(Character.toString( sql.charAt(i)) );
						}
				lastPos = i;
			}
				
		}
		String token = sql.substring(lastPos + 1);
		if(!token.trim().isEmpty()) {
			l.add(token.trim());
		}
		return l;
	}
	
	private static String getColName(String raw)  {
//		if(!raw.startsWith("`")) {
//			throw new RuntimeException();
//		}
		return StringUtil.dropAll(raw, '`');
	}

	@Override
	public void readColumns(AbstractTable tbl, Connection conn) throws SQLException {
		if (stColumndata==null)
			stColumndata = conn.prepareStatement("select * from sqlite_master where tbl_name = ?");
		stColumndata.setString(1, tbl.getName());
		ResultSet rsColumndata = stColumndata.executeQuery();
		PrimaryKey pk = new PrimaryKey();
		
		if(rsColumndata.next()) {
			String sql = rsColumndata.getString("sql");
			List<String> l = getTokenList(sql);
			
			boolean endOfColumnList = false;
			int index=0;
			int ordinalPosition = 0;
		
			expectToken(l, index++, "CREATE");
			expectToken(l, index++, "TABLE");
			index++;
			expectToken(l, index++, "(");
			while(!endOfColumnList) {
				Column col = makeColumnInstance(tbl);
				tbl.addColumn(col);
				col.setName(getColName(l.get(index++)));
				
				String type = l.get(index++);
				switch(type.toUpperCase()) {
				case "VARYING":
					expectToken(l, index++, "CHARACTER");
					type += " " + l.get(index);
					break;
				case "NATIVE":
					expectToken(l, index++, "CHARACTER");
					type += " " + l.get(index);
					break;
				case "DOUBLE":
					if(isTokenAt(l, index, "PRECISION")) {
						type += " " + l.get(index);
						index++;
					}
					break;
				case "DECIMAL":
					expectToken(l, index++, "(");
					expectInteger(l, index++);
					expectToken(l, index++, ",");
					expectInteger(l, index++);
					expectToken(l, index++, ")");
					break;
				case "NUMERIC":
					expectToken(l, index++, "(");
					expectInteger(l, index++);
					expectToken(l, index++, ",");
					expectInteger(l, index++);
					expectToken(l, index++, ")");
					break;	
				case "VARCHAR":
					expectToken(l, index++, "(");
					expectInteger(l, index++);
					expectToken(l, index++, ")");
					break;
				default:
						break;
				}
				col.setDbType(type);
				col.setNullable(!(isTokenAt(l, index, "NOT") && isTokenAt(l, index+1, "NULL")));
				if(!col.isNullable()) {
					index += 2;
				}
				if(isTokenAt(l, index, "DEFAULT")) {
					index++;
					col.setDefaultValue(l.get(index++));
				}
				
				if(isTokenAt(l, index, "PRIMARY")) {
					index++;
					expectToken(l, index++, "KEY");
					col.setNullable(false);
					pk.add(col);
				}
				if(isTokenAt(l, index, "AUTOINCREMENT")) {
					index++;
					col.setAutoIncrement(true);
				}
				
				col.setPosition(ordinalPosition++);
				if(l.get(index).equals(")") || (
						(isTokenAt(l, index, ",") && 
								(isTokenAt(l, index+1, "FOREIGN")
										||isTokenAt(l, index+1, "PRIMARY")
					)))){
					
					endOfColumnList = true;
				}
				index++;
			}
			
			while(index < l.size()) {
				if(isTokenAt(l, index, "FOREIGN")) {
					index++;
					expectToken(l, index++, "KEY");
					expectToken(l, index++, "(");
					do {
						index++;
					} while(l.get(index).equals(","));
					expectToken(l, index++, ")");
					expectToken(l, index++, "REFERENCES");
					index++;
					expectToken(l, index++, "(");
					do {
						index++;
					} while(l.get(index).equals(","));
					expectToken(l, index++, ")");
					
					
					if(isTokenAt(l, index, "ON")) {
						index++;
						expectToken(l, index++, "DELETE", "UPDATE");
						expectToken(l, index++, "CASCADE", "RESTRICT");
						expectToken(l, index++, "ON");
						expectToken(l, index++, "DELETE", "UPDATE");
						expectToken(l, index++, "CASCADE", "RESTRICT");
					}
					
					if(isTokenAt(l, index, ")")) {
						break;
					} else {
						expectToken(l, index++, ",");
						
					}
				} else if(isTokenAt(l, index, "PRIMARY")) {
					index++;
					expectToken(l, index++, "KEY");
					expectToken(l, index++, "(");
					do {
						pk.add(tbl.getColumnByName(StringUtil.dropAll(l.get(index++), '`')));
						
					} while(l.get(index++).equals(","));
					
					if(isTokenAt(l, index, ")")) {
						break;
					} else {
						expectToken(l, index++, ",");
					}
				} else {
					throw new RuntimeException("unexpected token " + l.get(index));
				}
			}
		}
		
		

		rsColumndata.close();
		tbl.setPrimaryKey(pk);
	}

	@Override
	public String sqlInsertOrUpdate(AbstractTable tbl, List<String> columns) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sqlInsert(AbstractTable tbl) {
		ArrayList<String> colNames = new ArrayList<>(); 
		for(Column c:tbl.getAllColumns()) {
			colNames.add(c.getEscapedName());
		}
		return String.format("insert into %s (%s) values (%s)",tbl.getEscapedName(),CodeUtil.commaSep(colNames),CodeUtil2.strMultiply("?", ",", colNames.size()));
	}

	@Override
	public String sqlInsertOrIgnoreMultiRow(AbstractTable tbl, String placeholders) {
		// TODO Auto-generated method stub
		return null;
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
	public String getBooleanExpressionTrue() {
		return "1";
	}

	@Override
	public String getDefaultSchema() {
		return null;
	}

	@Override
	public boolean supportsInsertOrIgnore() {
		return false;
	}

	@Override
	public boolean supportsMultiRowInsert() {
		return true;
	}


	@Override
	public String getEscapedTableName(AbstractTable tbl) {
		return String.format("\"%s\"", tbl.getName());
	}


	@Override
	public Column makeColumnInstance(AbstractTable tbl) {
		return new SqliteColumn(tbl);
	}

	private static void expectInteger(List<String> l, int index) {
		if(!l.get(index).matches("[0-9]*")) {
			throw new RuntimeException("expected integer at "+ index);
		}
	}
	
	private static void expectToken(List<String> l, int index, String...expect) {
		if(expect.length == 0) {
			throw new IllegalArgumentException();
		}
		for(String e:expect) {
			if(l.get(index).toUpperCase().equals(e.toUpperCase())) {
				return;
			}
		}
		if(expect.length>1)
			throw new RuntimeException("expected any of " + Arrays.toString(expect)+ " but got " +l.get(index));
		else
			throw new RuntimeException("expected " + expect[0]+ " but got " +l.get(index));
	}
	
	private static boolean isTokenAt(List<String> l, int index, String...expect) {
		for(String e:expect) {
			if(l.get(index).toUpperCase().equals(e.toUpperCase())) {
				return true;
			}
		}
		return false;
	}
}
