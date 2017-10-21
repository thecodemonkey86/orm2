package cpp.lib;

import cpp.Types;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.ClsTemplate;
import cpp.core.TplCls;

public class ClsAbstractBeanQuery extends TplCls{

	public ClsAbstractBeanQuery(ClsTemplate tpl, Cls cls) {
		super(tpl.getName(),cls);
		addMethod(new LibMethod(toRef(), "select"));
		addMethod(new LibMethod(toRef(), "leftJoin"));
		addMethod(new LibMethod(Types.QSqlQuery.toUniquePointer(), "execQuery"));
		addAttr(new Attr(Types.Sql.toRawPointer(), "sqlCon"));
		addAttr(new Attr(Types.SqlQuery.toUniquePointer(), "qu"));
	}

}
