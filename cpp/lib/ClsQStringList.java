package cpp.lib;

import cpp.CoreTypes;
import cpp.core.Cls;

public class ClsQStringList extends Cls {

	public ClsQStringList() {
		super("QStringList");
		addMethod(new LibMethod(CoreTypes.Void, "append"));
		addMethod(new LibMethod(CoreTypes.Void, "reserve"));
	}

}
