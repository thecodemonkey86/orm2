package cpp.lib;

import cpp.Types;
import cpp.core.Cls;

public class ClsSqlQuery extends Cls{

	public static String orderBy = "orderBy";

	public ClsSqlQuery() {
		super("SqlQuery");
		addMethod(new LibMethod(this.toRawPointer(), "select"));
		addMethod(new LibMethod(this.toRawPointer(), "from"));
		addMethod(new LibMethod(this.toRawPointer(), "leftJoin"));
		addMethod(new LibMethod(this.toRawPointer(), "join"));
		addMethod(new LibMethod(Types.QSqlQuery, "execQuery"));
		addMethod(new LibMethod(this.toRawPointer(), "where"));
		addMethod(new LibMethod(this.toRawPointer(), "andWhereIn"));
		addMethod(new LibMethod(this.toRawPointer(), "whereIn"));
		addMethod(new LibMethod(this.toRawPointer(), orderBy));
	}

}
