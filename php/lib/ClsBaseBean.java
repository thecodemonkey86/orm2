package php.lib;

import php.Types;
import php.cls.Attr;
import php.cls.PhpCls;
import php.cls.method.MethodAttributeSetter;

public class ClsBaseBean extends PhpCls{

	public static final String setLoaded = "setLoaded";
	public static final String METHOD_NAME_IS_INSERT_NEW = "isInsertNew";
	public static final String METHOD_NAME_IS_AUTO_INCREMENT = "isAutoIncrement";
	public static final String METHOD_NAME_SET_AUTO_INCREMENT_ID = "setAutoIncrementId";
	public static final String METHOD_NAME_HAS_UPDATE = "hasUpdate";
	public static final String METHOD_NAME_IS_PRIMARY_KEY_MODIFIED = "isPrimaryKeyModified";

	public ClsBaseBean() {
		super("BaseBean", "PhpLibs\\Orm\\Model");
		//Attr attrSqlCon = new Attr(Types.Sql, "sqlCon");
//		attrSqlCon.setStatic(true);
		//addAttr(attrSqlCon);
		Attr attrAutoIncrement = new Attr(Types.Bool, "autoIncrement");
		addAttr(attrAutoIncrement);
		Attr attrInsert = new Attr(Types.Bool, "insert");
		addAttr(attrInsert);
		addAttr(new Attr(Types.Bool, "primaryKeyModified"));
		addMethod(new LibMethod(Types.Void, "setInsertNew"));
		addMethod(new LibMethod(Types.Void, setLoaded));
		Attr aLoaded = new Attr(Types.Bool, "loaded");
		addMethod(new LibMethod(Types.Bool, METHOD_NAME_IS_INSERT_NEW));
		addMethod(new LibMethod(Types.Bool, METHOD_NAME_IS_AUTO_INCREMENT));
		addMethod(new LibMethod(Types.Bool, METHOD_NAME_HAS_UPDATE));
		addMethod(new LibMethod(Types.Bool, METHOD_NAME_IS_PRIMARY_KEY_MODIFIED));
		addAttr(aLoaded);
	}
}
