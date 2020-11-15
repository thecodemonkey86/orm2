package cpp.lib;

import cpp.Namespaces;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Cls;

public class ClsSqlQuery extends Cls{

	public static final String orderBy = "orderBy";

	public ClsSqlQuery() {
		super("SqlQuery");
		setUseNamespace(Namespaces.SqlUtil3);
		addMethod(new LibMethod(this.toRef(), "select"));
		addMethod(new LibMethod(this.toRef(), "from"));
		addMethod(new LibMethod(this.toRef(), "leftJoin"));
		addMethod(new LibMethod(this.toRef(), "join"));
		addMethod(new LibMethod(Types.QSqlQuery, "execQuery"));
		addMethod(new LibMethod(this.toRef(), "where"));
		addMethod(new LibMethod(this.toRef(), "andWhereIn"));
		addMethod(new LibMethod(this.toRef(), "whereIn"));
		addMethod(new LibMethod(this.toRef(), orderBy));
		addAttr(new Attr(Attr.Public, Types.QString, "AND", null, true));
	}

}
