package cpp.cls.bean;

import cpp.Types;
import cpp.cls.TplCls;
import cpp.cls.Type;
import cpp.lib.LibMethod;

public class Nullable extends TplCls{

	public Nullable( Type element) {
		super("Nullable", element);
		addMethod(new LibMethod(Types.Void, "setNull"));
		addMethod(new LibMethod(Types.Bool, "isNull"));
		addMethod(new LibMethod(element, "val"));
	}

}
