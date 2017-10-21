package sunjava.lib;

import sunjava.core.JavaGenericClass;
import sunjava.core.Type;
import sunjava.core.Types;

public class ClsLinkedHashSet extends JavaGenericClass {

	public static final String add = "add";
	public static final String remove = "remove";
	
	public ClsLinkedHashSet(Type elementType) {
		super("LinkedHashSet",elementType, "java.util");
		addMethod(new LibMethod(Types.Bool, add));
		addMethod(new LibMethod(Types.Bool, remove));
		addMethod(new LibMethod(Types.Bool, "size"));
	}


	

}
