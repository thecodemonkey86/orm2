package cpp.lib;

import cpp.CoreTypes;
import cpp.core.TplCls;
import cpp.core.Type;

public class ClsQSet extends TplCls{

	public static String contains = "contains";
	
	public ClsQSet(Type element) {
		super("QSet", element);
		addMethod(new LibMethod(CoreTypes.Bool, contains));
		addMethod(new LibMethod(CoreTypes.Void, "insert"));
		addMethod(new LibMethod(CoreTypes.Int, "size"));
	}

}
