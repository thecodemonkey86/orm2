package cpp.entityquery.method;

import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.entity.EntityCls;
import cpp.entityquery.EntityQueryType;
import cpp.lib.ClsAbstractEntityQuery;
import cpp.lib.ClsQVariant;
import database.column.Column;
import cpp.core.Type;

public class MethodEntityQueryWhereEquals extends Method{
	EntityCls entity;
	Param pValue ;
	Column c;
	EntityQueryType entityQueryType;
	
	public MethodEntityQueryWhereEquals(Cls query,EntityQueryType entityQueryType, EntityCls entity,Column c) {
		super(Public, query.toRef(), getMethodName(c));
		this.entity=entity;
		Type t = EntityCls.getDatabaseMapper().columnToType(c,false);
		pValue = addParam(new Param( t.isPrimitiveType() ? t : t.toConstRef(), "value"));
		this.c = c;
		this.entityQueryType = entityQueryType;
	}

	public static String getMethodName(Column c) {
		return "where"+c.getUc1stCamelCaseName()+"Equals";
	}

	@Override
	public void addImplementation() {
		
		if(entityQueryType == EntityQueryType.Select) {
			_return( _this().callMethod(ClsAbstractEntityQuery.where,  QString.fromStringConstant("e1." +  c.getEscapedName()+"=?"), ClsQVariant.fromValue(pValue) ));
		} else {
			_return( _this().callMethod(ClsAbstractEntityQuery.where, QString.fromStringConstant( c.getEscapedName()+"=?"), ClsQVariant.fromValue(pValue) ));
		}
		
		
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
