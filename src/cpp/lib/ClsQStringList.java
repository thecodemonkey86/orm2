package cpp.lib;

import cpp.CoreTypes;
import cpp.core.Cls;

public class ClsQStringList extends Cls {

	public static final String append = "append";
	public static final String reserve = "reserve";
	public static final String isEmpty = "isEmpty";
	
	public ClsQStringList() {
		super("QStringList");
		addMethod(new LibMethod(CoreTypes.Void, append));
		addMethod(new LibMethod(CoreTypes.Void, reserve));
		addMethod(new LibMethod(CoreTypes.Bool, isEmpty));
	}

}
