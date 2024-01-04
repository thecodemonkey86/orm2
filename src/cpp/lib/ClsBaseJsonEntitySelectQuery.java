package cpp.lib;

import cpp.CoreTypes;
import cpp.Namespaces;
import cpp.core.Cls;

public class ClsBaseJsonEntitySelectQuery extends Cls {

	public static final String where="where";
	public static final String join="join";
	public static final String orderBy="orderBy";
	public static final String toJsonString="toJsonString";
	
	public ClsBaseJsonEntitySelectQuery() {
		super("BaseJsonEntitySelectQuery");
		setUseNamespace(Namespaces.ORM2);
		addMethod(new LibMethod(this, where));
		addMethod(new LibMethod(this, join));
		addMethod(new LibMethod(this, orderBy));
		addMethod(new LibMethod(CoreTypes.QString, toJsonString));
	}

}
