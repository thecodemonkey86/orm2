package cpp.util;

import cpp.CoreTypes;
import cpp.core.Cls;
import cpp.lib.LibMethod;

public class ClsDbPool extends Cls {
	public static ClsDbPool instance;
	public static final String getDatabase="getDatabase";
	
	public ClsDbPool(String name, String hdr) {
		super(name);
		headerInclude = hdr;
		addMethod(new LibMethod(CoreTypes.QSqlDatabase, getDatabase, true));
	}

	public static void setInstance(ClsDbPool instance) {
		ClsDbPool.instance = instance;
	}
}
