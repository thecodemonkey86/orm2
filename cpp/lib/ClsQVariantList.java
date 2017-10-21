package cpp.lib;

import cpp.Types;
import cpp.core.Cls;

public class ClsQVariantList extends Cls{

	public ClsQVariantList() {
		super("QVariantList");
		addMethod(new LibMethod(Types.Void, "append"));
		addMethod(new LibMethod(Types.Void, "reserve"));
	}

}
