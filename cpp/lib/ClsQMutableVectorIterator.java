package cpp.lib;

import cpp.Types;
import cpp.core.TplCls;
import cpp.core.Type;

public class ClsQMutableVectorIterator extends TplCls{

	public static final String hasNext = "hasNext";
	public static final String next = "next";
	public static final String remove = "remove";
	
	public ClsQMutableVectorIterator(Type element) {
		super("QMutableVectorIterator", element);
		addMethod(new LibMethod(Types.Bool, hasNext));
		addMethod(new LibMethod(element, next));
		addMethod(new LibMethod(Types.Void, remove));
	}

}
