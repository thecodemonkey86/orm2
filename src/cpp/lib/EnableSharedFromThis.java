package cpp.lib;

import cpp.core.Cls;
import cpp.core.TplCls;
import cpp.core.Type;

public class EnableSharedFromThis extends TplCls{

	public static final String SHARED_FROM_THIS = "shared_from_this";

	public EnableSharedFromThis( Type element) {
		super("enable_shared_from_this", element);
		addMethod(new LibMethod(((Cls)element).toSharedPtr(), SHARED_FROM_THIS));
		setUseNamespace("std");
	}

}
