package php.lib;

import php.core.PhpPseudoGenericClass;
import php.core.Type;
import php.core.Types;

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
