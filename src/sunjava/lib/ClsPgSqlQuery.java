package sunjava.lib;

import sunjava.core.JavaCls;
import sunjava.core.Types;

public class ClsPgSqlQuery extends JavaCls{

	public ClsPgSqlQuery() {
		super("PgSqlQuery","sql.query");
		setSuperclass(Types.SqlQuery);
	}

}
