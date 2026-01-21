package cpp.openapi;

import database.column.Column;

public abstract class OpenApiYamlDatabaseTypeMapper {
	public abstract YamlType toYamlType(Column col); 
}
