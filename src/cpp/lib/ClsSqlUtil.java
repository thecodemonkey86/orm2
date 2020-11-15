package cpp.lib;

import cpp.Namespaces;
import cpp.Types;
import cpp.core.Cls;
import cpp.core.Type;

public class ClsSqlUtil extends Cls {

	public static final String getPlaceholders ="getPlaceholders" ;
	
	public ClsSqlUtil() {
		super("SqlUtil");
		setUseNamespace(Namespaces.SqlUtil3);
		addMethod(new LibMethod(Types.QString,getPlaceholders,true ));
	}
	
	@Override
	public Type toRawPointer() {
		throw new RuntimeException("private constructor. No instance");
	}

}
