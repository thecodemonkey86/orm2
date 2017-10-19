package php.lib;

import php.Types;
import php.cls.Attr;
import php.cls.Method;
import php.cls.PhpCls;
import php.cls.PhpPseudoGenericClass;

public class ClsBaseBeanQuery extends PhpPseudoGenericClass{

	public static final String mainBeanAlias = "mainBeanAlias";
	public static final String query = "query";

	public ClsBaseBeanQuery( PhpCls cls) {
		super("BeanQuery",cls,"Sql\\Orm");
		addMethod(new LibMethod(this, "select"));
		addMethod(new LibMethod(this, "leftJoin"));
		addMethod(new LibMethod(Types.mysqli_result, "query"));
		addAttr(new Attr(Types.String, mainBeanAlias));
	}

}
