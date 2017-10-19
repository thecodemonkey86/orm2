package sunjava.lib;

import sunjava.Types;
import sunjava.cls.JavaGenericClass;
import sunjava.cls.Type;

public class ClsHashSet extends JavaGenericClass{

	public static final String add = "add";
	public static final String contains = "contains";
	public static final String size = "size";

	public ClsHashSet(Type element) {
		super("HashSet", element, "java.util");
		addMethod(new LibMethod(Types.Bool, contains));
		addMethod(new LibMethod(Types.Void, add));
		addMethod(new LibMethod(Types.Int,  size));
	}

}
