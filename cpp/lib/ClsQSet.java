package cpp.lib;

import cpp.Types;
import cpp.cls.TplCls;
import cpp.cls.Type;

public class ClsQSet extends TplCls{

	public ClsQSet(Type element) {
		super("QSet", element);
		addMethod(new LibMethod(Types.Bool, "contains"));
		addMethod(new LibMethod(Types.Void, "insert"));
		addMethod(new LibMethod(Types.Int, "size"));
	}

}
