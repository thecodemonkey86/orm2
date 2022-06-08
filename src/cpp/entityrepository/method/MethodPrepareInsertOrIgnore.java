package cpp.entityrepository.method;

import cpp.Types;
import cpp.core.Method;
import cpp.entity.EntityCls;

public class MethodPrepareInsertOrIgnore extends Method {
	EntityCls entity;
	public MethodPrepareInsertOrIgnore(EntityCls entity) {
		super(Public, Types.QSqlQuery, "prepareInsertOrIgnore"+entity.getName());
		this.entity = entity;
	}

	@Override
	public void addImplementation() {
		_return(_this().callMethod(parent.getTemplateMethod(EntityCls.getDatabaseMapper().getRepositoryPrepareInsertOrIgnoreMethod(), entity)  )) ;

	}

}
