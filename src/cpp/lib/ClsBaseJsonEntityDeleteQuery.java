package cpp.lib;

import cpp.CoreTypes;
import cpp.Namespaces;
import cpp.Types;
import cpp.core.Cls;

public class ClsBaseJsonEntityDeleteQuery extends Cls {

	public static final String where="where";
	public static final String exec="exec";
	public static final String toJsonString="toJsonString";
	
	public ClsBaseJsonEntityDeleteQuery() {
		super("BaseJsonEntityDeleteQuery");
		setUseNamespace(Namespaces.ORM2);
		addMethod(new LibMethod(this, where));
		addMethod(new LibMethod(Types.Void, exec));
		addMethod(new LibMethod(CoreTypes.QString, toJsonString));
	}

}
