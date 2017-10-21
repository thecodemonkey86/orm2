package php.lib;

import php.core.Attr;
import php.core.PhpCls;
import php.core.Types;

public class ClsMySqlQuery extends PhpCls{

	public static final String join = "join";
	public static final String leftJoin = "leftJoin";
	public static final String from = "from";
	public static final String select = "select";
	public static final String query = "query";
	public static final String where = "where";
	
	public ClsMySqlQuery() {
		super("MySqlQuery","PhpLibs\\Sql\\Query");
		addAttr(new Attr(Types.mysqli, "sqlCon"));
		addMethod(new LibMethod(Types.mysqli_result, query));
		addMethod(new LibMethod(this, select));
		addMethod(new LibMethod(this, from));
		addMethod(new LibMethod(this, leftJoin));
		addMethod(new LibMethod(this, join));
		
		addMethod(new LibMethod(this, where));
		setSuperclass(Types.SqlQuery);
	}

}
