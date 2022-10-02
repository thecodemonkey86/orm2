package cpp.entityquery.method;

import cpp.core.Method;
import cpp.entity.EntityCls;
import cpp.entityrepository.method.MethodGetFromRecord;
import cpp.lib.ClsQSqlQuery;

public class MethodQueryIteratorGet extends Method {
	EntityCls entity;
	public MethodQueryIteratorGet(EntityCls entity) {
		super(Method.Public, entity.toSharedPtr(), "get");
		this.entity = entity;
	}

	@Override
	public void addImplementation() {
		_this().accessAttr("repository").callMethod(MethodGetFromRecord.getMethodName(entity),_this().accessAttr("query").callMethod(ClsQSqlQuery.record));
	}

}
