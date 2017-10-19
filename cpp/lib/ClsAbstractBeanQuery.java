package cpp.lib;

import cpp.Types;
import cpp.cls.Attr;
import cpp.cls.Cls;
import cpp.cls.ClsTemplate;
import cpp.cls.TplCls;

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
