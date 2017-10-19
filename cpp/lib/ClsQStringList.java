package cpp.lib;

import cpp.Types;
import cpp.cls.Cls;

public class ClsQStringList extends Cls {

	public ClsQStringList() {
		super("QStringList");
		addMethod(new LibMethod(Types.Void, "append"));
		addMethod(new LibMethod(Types.Void, "reserve"));
	}

}
