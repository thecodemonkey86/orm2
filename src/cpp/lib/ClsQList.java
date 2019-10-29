package cpp.lib;

import cpp.CoreTypes;
import cpp.core.TplCls;
import cpp.core.Type;

public class ClsQList extends TplCls{

	public static final String contains = "contains";
	public static final String insert = "insert";
	public static final String size = "size";
	
	public ClsQList(Type element) {
		super("QList", element);
		addMethod(new LibMethod(CoreTypes.Bool, contains));
		addMethod(new LibMethod(CoreTypes.Void, insert));
		addMethod(new LibMethod(CoreTypes.Int, size));
	}

}
