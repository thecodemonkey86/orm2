package cpp.entityquery.method;

import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.QString;
import cpp.entity.EntityCls;
import cpp.entityquery.EntityQueryType;
import cpp.lib.ClsAbstractEntityQuery;
import database.column.Column;

public class MethodEntityQueryWhereIsNotNull extends Method{
	EntityCls entity;
	EntityQueryType entityQueryType;
	Column c;
	public MethodEntityQueryWhereIsNotNull(Cls query,EntityQueryType entityQueryType, EntityCls entity,Column c) {
		super(Public, query.toRef(), "where"+c.getUc1stCamelCaseName()+"IsNotNull");
		this.entity=entity;
		this.c = c;
		this.entityQueryType = entityQueryType;
	}

	@Override
	public void addImplementation() {
		if(entityQueryType == EntityQueryType.Select) {
			_return( _this().callMethod(ClsAbstractEntityQuery.where,QString.fromStringConstant("e1." + c.getEscapedName()+" is not null")));
		} else {
			_return( _this().callMethod(ClsAbstractEntityQuery.where,QString.fromStringConstant(c.getEscapedName()+" is not null")));
		}
		
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
