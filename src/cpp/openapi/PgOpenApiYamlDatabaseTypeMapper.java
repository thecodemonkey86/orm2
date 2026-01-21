package cpp.openapi;

 
import database.column.Column;

public class PgOpenApiYamlDatabaseTypeMapper extends OpenApiYamlDatabaseTypeMapper{
 
	@Override
	public YamlType toYamlType(Column col) {
		//boolean nullable = col.isNullable();
		String dbType = col.getDbType();
		switch(dbType) {
		case "integer":
			return YamlType.INT32;
		case "bigint":
			return YamlType.INT64;
		case "smallint":
			return YamlType.INT16;
		case "character varying":
		case "character":	
		case "text":
			return YamlType.STRING;
		case "date":
			return YamlType.DATE;
		case "double precision":
		case "numeric":
		case "real":
			return YamlType.DOUBLE;
		case "bytea":
			return YamlType.STRING;
		case "boolean":
			return YamlType.BOOLEAN;
		case "timestamp with time zone":
		case "timestamp without time zone":
			return YamlType.DATETIME;
		case "time with time zone":
		case "time without time zone":
			return YamlType.STRING;
		}
		System.out.print(col.getDbType());
		throw new UnsupportedOperationException();
	}
	
}
