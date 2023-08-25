package sunjava.lib;

import sunjava.core.Attr;
import sunjava.core.JavaCls;
import sunjava.core.JavaGenericClass;
import sunjava.core.Types;

public class ClsBaseEntityQuery extends JavaGenericClass{

	public static final String MAIN_ENTITY_ALIAS = "MAIN_ENTITY_ALIAS";

	public ClsBaseEntityQuery( JavaCls cls) {
		super("EntityQuery",cls,"sql.orm.query");
		setSuperclass(Types.SqlQuery);
		addMethod(new LibMethod(this, "select"));
		addMethod(new LibMethod(this, "leftJoin"));
		addAttr(new Attr(Types.String, MAIN_ENTITY_ALIAS));
	}

}
