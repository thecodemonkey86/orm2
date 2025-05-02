package cpp.entityquery.method;

import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.QString;
import cpp.entity.EntityCls;
import cpp.entityquery.EntityQueryType;
import cpp.lib.ClsAbstractEntityQuery;
import database.column.Column;

public class MethodEntityQueryWhereIsNull extends Method{
	EntityCls entity;
	EntityQueryType entityQueryType;
	Column c;
	public MethodEntityQueryWhereIsNull(Cls query,EntityQueryType entityQueryType, EntityCls entity,Column c) {
		super(Public, query.toRef(), "where"+c.getUc1stCamelCaseName()+"IsNull");
		this.entity=entity;
		this.c = c;
		this.entityQueryType = entityQueryType;
	}

	@Override
	public void addImplementation() {
		if(entityQueryType == EntityQueryType.Select) {
			_return( _this().callMethod(ClsAbstractEntityQuery.where,QString.fromStringConstant("e1." + c.getEscapedName()+" is null")));
		} else {
			_return( _this().callMethod(ClsAbstractEntityQuery.where,QString.fromStringConstant(c.getEscapedName()+" is null")));
		}
		
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
