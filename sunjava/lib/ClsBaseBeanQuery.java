package sunjava.lib;

import sunjava.core.Attr;
import sunjava.core.JavaCls;
import sunjava.core.JavaGenericClass;
import sunjava.core.Types;

public class ClsBaseBeanQuery extends JavaGenericClass{

	public static final String mainBeanAlias = "mainBeanAlias";

	public ClsBaseBeanQuery( JavaCls cls) {
		super("EntityQuery",cls,"sql.orm");
		setSuperclass(Types.SqlQuery);
		addMethod(new LibMethod(this, "select"));
		addMethod(new LibMethod(this, "leftJoin"));
		addAttr(new Attr(Types.String, mainBeanAlias));
	}

}
