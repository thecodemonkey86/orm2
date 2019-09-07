package php.lib;

import php.bean.BeanCls;
import php.core.Attr;
import php.core.PhpPseudoGenericClass;
import php.core.Types;

public class ClsBaseBeanQuery extends PhpPseudoGenericClass{

	public static final String sqlQuery = "sqlQuery";
	public static final String mainBeanAlias = "mainBeanAlias";
	public static final String where = "where";
	public static final String join = "join";
	public static final String addInsertRawExpression = "addInsertRawExpression";
	public static final String select = "select";

	public ClsBaseBeanQuery( BeanCls cls) {
		super("EntityQuery",cls,"PhpLibs\\Orm\\Query");
		addAttr(new Attr(Types.Int, "entityQueryLimit"));
		addAttr(new Attr(Types.Int, "entityQueryOffset"));
		addAttr(new Attr(Types.SqlQuery, sqlQuery));
		addMethod(new LibMethod(this, select));
		addMethod(new LibMethod(this, "leftJoin"));
		addMethod(new LibMethod(this, join));
		addMethod(new LibMethod(this, where));
		addMethod(new LibMethod(this, addInsertRawExpression));
//		addMethod(new LibMethod(Types.mysqli_result, "query"));
		addAttr(new Attr(Types.String, mainBeanAlias));
	}

}
