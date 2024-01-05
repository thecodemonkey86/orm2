package cpp.jsonentity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import database.table.Table;

public class JsonEntities {
private static Map<String,JsonEntity> entities;
	
	static {
		entities = new HashMap<>();
	}
	
	public static JsonEntity get(Table tbl) {
		return get(tbl.getUc1stCamelCaseName());
	}
	
	public static JsonEntity get(String name) {
		if (!entities.containsKey(name)) {
			throw new RuntimeException("Cls not found "+name); 
		}
		return entities.get(name);
	}
	
	public static void add(JsonEntity cls) {
		entities.put(cls.getName(), cls);
	}
	
	public static Collection<JsonEntity> getAllEntities() {
		return entities.values();
	}
}
