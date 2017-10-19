package sunjava.lib;

import sunjava.Types;
import sunjava.cls.Attr;
import sunjava.cls.JavaCls;
import sunjava.cls.JavaGenericClass;

public class ClsBaseBeanQuery extends JavaGenericClass{

	public static final String mainBeanAlias = "mainBeanAlias";

	public ClsBaseBeanQuery( JavaCls cls) {
		super("BeanQuery",cls,"sql.orm");
		setSuperclass(Types.SqlQuery);
		addMethod(new LibMethod(this, "select"));
		addMethod(new LibMethod(this, "leftJoin"));
		addAttr(new Attr(Types.String, mainBeanAlias));
	}

}
