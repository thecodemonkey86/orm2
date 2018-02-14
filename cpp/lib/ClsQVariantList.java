package cpp.lib;

import cpp.CoreTypes;
import cpp.core.Cls;

public class ClsQVariantList extends Cls{

	public static final String append = "append";
	
	public ClsQVariantList() {
		super("QVariantList");
		addMethod(new LibMethod(CoreTypes.Void, append));
		addMethod(new LibMethod(CoreTypes.Void, "reserve"));
	}

}
