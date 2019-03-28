package sunjava.lib;


import sunjava.core.JavaGenericClass;
import sunjava.core.Type;
import sunjava.core.Types;

public class ClsArrayList extends JavaGenericClass {

	public static final String add = "add";
	public static final String ensureCapacity = "ensureCapacity";
	public static final String remove = "remove";
	public static final String isEmpty = "isEmpty";
	public static final String size = "size";
	public static final String get = "get";
	
	public static final String CLSNAME="ArrayList";
	
	public ClsArrayList(Type element) {
		super(CLSNAME, element,"java.util");
		addMethod(new LibMethod(Types.Void, add));
		addMethod(new LibMethod(Types.Void, remove));
		addMethod(new LibMethod(Types.Bool, isEmpty));
		addMethod(new LibMethod(Types.Int, size));
		addMethod(new LibMethod(Types.Int, ensureCapacity));
		addMethod(new LibMethod(element, get));
		
	}


}
