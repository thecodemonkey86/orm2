package cpp.entityquery.method;

import cpp.CoreTypes;
import cpp.core.Method;
import cpp.lib.ClsQSqlQuery;

public class MethodQueryIteratorNext extends Method {

	public MethodQueryIteratorNext() {
		super(Method.Public, CoreTypes.Bool, "next");
	}

	@Override
	public void addImplementation() {
		_return( _this().accessAttr("query").callMethod(ClsQSqlQuery.next));
	}

}
