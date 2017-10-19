package php.lib;

import php.Types;
import php.cls.PhpPseudoGenericClass;
import php.cls.Type;

public class ClsLinkedHashSet extends PhpPseudoGenericClass {

	public static final String add = "add";
	public static final String remove = "remove";
	
	public ClsLinkedHashSet(Type elementType) {
		super("LinkedHashSet",elementType, "java.util");
		addMethod(new LibMethod(Types.Bool, add));
		addMethod(new LibMethod(Types.Bool, remove));
		addMethod(new LibMethod(Types.Bool, "size"));
	}


	

}
