package sunjava.lib;

import sunjava.Types;
import sunjava.cls.JavaCls;

public class ClsPgSqlQuery extends JavaCls{

	public ClsPgSqlQuery() {
		super("PgSqlQuery","sql.query");
		setSuperclass(Types.SqlQuery);
	}

}
