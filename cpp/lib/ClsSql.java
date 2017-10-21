package cpp.lib;

import cpp.Types;
import cpp.core.Cls;

public class ClsSql extends Cls {

	public static final String buildQuery = "buildQuery";

	public ClsSql() {
		super("Sql");
		addMethod(new LibMethod(Types.SqlQuery.toSharedPtr(), buildQuery));
		addMethod(new LibMethod(Types.Bool, "execute"));
	}

}
