package sunjava.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import database.table.Table;

public class Entities {
	private static Map<String,EntityCls> entities;
	
	static {
		entities = new HashMap<>();
	}
	
	public static EntityCls get(Table tbl) {
		return get(tbl.getUc1stCamelCaseName());
	}
	
	public static EntityCls get(String name) {
		if (!entities.containsKey(name)) {
			throw new RuntimeException("Cls not found "+name); 
		}
		return entities.get(name);
	}
	
	public static void add(EntityCls cls) {
		entities.put(cls.getName(), cls);
	}
	
	public static Collection<EntityCls> getAllBeans() {
		return entities.values();
	}
}
