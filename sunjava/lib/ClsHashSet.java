package sunjava.lib;

import sunjava.core.JavaGenericClass;
import sunjava.core.Type;
import sunjava.core.Types;

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
