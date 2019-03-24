package sunjava.bean;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import database.table.Table;

public class Beans {
	private static Map<String,BeanCls> beans;
	
	static {
		beans = new HashMap<>();
	}
	
	public static BeanCls get(Table tbl) {
		return get(tbl.getUc1stCamelCaseName());
	}
	
	public static BeanCls get(String name) {
		if (!beans.containsKey(name)) {
			throw new RuntimeException("Cls not found "+name); 
		}
		return beans.get(name);
	}
	
	public static void add(BeanCls cls) {
		beans.put(cls.getName(), cls);
	}
	
	public static Collection<BeanCls> getAllBeans() {
		return beans.values();
	}
}
