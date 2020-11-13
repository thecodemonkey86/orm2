package cpp.entityrepository.method;

import cpp.Types;
import cpp.core.Method;
import cpp.entity.EntityCls;

public class MethodPrepareUpsert extends Method {
	EntityCls entity;
	public MethodPrepareUpsert(EntityCls entity) {
		super(Public, Types.QSqlQuery, "prepareInsertOrIgnore"+entity.getName());
		this.entity = entity;
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		_return(parent.callStaticMethod(parent.getTemplateMethod(EntityCls.getDatabaseMapper().getRepositoryPrepareInsertOrIgnoreMethod(), entity)  )) ;

	}

}
