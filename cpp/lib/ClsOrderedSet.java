package cpp.lib;

import cpp.Types;
import cpp.cls.TplCls;
import cpp.cls.Type;

public class ClsOrderedSet extends TplCls {

	
	public ClsOrderedSet(Type elementType) {
		super("OrderedSet",elementType);
		addMethod(new LibMethod(Types.qlist(elementType), "toList"));
		addMethod(new LibMethod(Types.Bool, "insert"));
		addMethod(new LibMethod(Types.Bool, "append"));
		addMethod(new LibMethod(Types.Bool, "size"));
		headerInclude = "util/orderedset";
	}


	

}
