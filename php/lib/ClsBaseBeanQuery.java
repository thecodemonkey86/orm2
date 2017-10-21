package php.lib;

import php.core.Attr;
import php.core.PhpCls;
import php.core.PhpPseudoGenericClass;
import php.core.Types;

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
