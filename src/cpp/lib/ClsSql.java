package cpp.lib;

import cpp.Namespaces;
import cpp.Types;
import cpp.core.Cls;
import cpp.core.Type;

public class ClsSql extends Cls {

	public static final String execute ="execute" ;
	
	public ClsSql() {
		super("Sql");
		setUseNamespace(Namespaces.SqlUtil4);
		addMethod(new LibMethod(Types.Bool,execute,true ));
	}
	
	@Override
	public Type toRawPointer() {
		throw new RuntimeException("private constructor. No instance");
	}

}
