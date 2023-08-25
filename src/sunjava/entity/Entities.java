package sunjava.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import database.table.Table;

public class Entities {
	private static Map<String,EntityCls> beans;
	
	static {
		beans = new HashMap<>();
	}
	
	public static EntityCls get(Table tbl) {
		return get(tbl.getUc1stCamelCaseName());
	}
	
	public static EntityCls get(String name) {
		if (!beans.containsKey(name)) {
			throw new RuntimeException("Cls not found "+name); 
		}
		return beans.get(name);
	}
	
	public static void add(EntityCls cls) {
		beans.put(cls.getName(), cls);
	}
	
	public static Collection<EntityCls> getAllBeans() {
		return beans.values();
	}
}
