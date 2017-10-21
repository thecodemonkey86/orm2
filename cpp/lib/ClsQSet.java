package cpp.lib;

import cpp.Types;
import cpp.core.TplCls;
import cpp.core.Type;

public class ClsQSet extends TplCls{

	public ClsQSet(Type element) {
		super("QSet", element);
		addMethod(new LibMethod(Types.Bool, "contains"));
		addMethod(new LibMethod(Types.Void, "insert"));
		addMethod(new LibMethod(Types.Int, "size"));
	}

}
