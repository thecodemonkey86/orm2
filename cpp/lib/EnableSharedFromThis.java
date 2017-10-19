package cpp.lib;

import cpp.cls.Cls;
import cpp.cls.TplCls;
import cpp.cls.Type;

public class EnableSharedFromThis extends TplCls{

	public static final String SHARED_FROM_THIS = "shared_from_this";

	public EnableSharedFromThis( Type element) {
		super("enable_shared_from_this", element);
		addMethod(new LibMethod(((Cls)element).toSharedPtr(), SHARED_FROM_THIS));
	}

}
