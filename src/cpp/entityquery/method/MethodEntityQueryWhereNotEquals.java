package cpp.entityquery.method;

import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.instruction.IfBlock;
import cpp.entity.EntityCls;
import cpp.core.Optional;
import cpp.entityquery.EntityQueryType;
import cpp.lib.ClsAbstractEntityQuery;
import cpp.lib.ClsQVariant;
import database.column.Column;
import cpp.core.Type;

public class MethodEntityQueryWhereNotEquals extends Method{
	EntityCls entity;
	Param pValue ;
	EntityQueryType entityQueryType;
	Column c;
	public MethodEntityQueryWhereNotEquals(Cls query,EntityQueryType entityQueryType, EntityCls entity,Column c) {
		super(Public, query.toRef(), "where"+c.getUc1stCamelCaseName()+"NotEquals");
		this.entity=entity;
		Type t = EntityCls.getDatabaseMapper().columnToType(c);
		pValue = addParam(new Param(t.isPrimitiveType() ? t : t.toConstRef(), "value"));
		this.c = c;
		this.entityQueryType = entityQueryType;
	}

	@Override
	public void addImplementation() {
		if(entityQueryType == EntityQueryType.Select) {
			if(c.isNullable()) {
				IfBlock ifNull = _ifNot(pValue.callMethod(Optional.has_value));
				ifNull.thenBlock()._return( _this().callMethod(ClsAbstractEntityQuery.where, QString.fromStringConstant("e1."+ c.getEscapedName()+" is not null"))) ;
				ifNull.elseBlock()._return( _this().callMethod(ClsAbstractEntityQuery.where, QString.fromStringConstant("e1."+c.getEscapedName()+"<>?"), ClsQVariant.fromValue( pValue.callMethod(Optional.value) ) ));
			} else {
				_return( _this().callMethod(ClsAbstractEntityQuery.where, QString.fromStringConstant( "e1."+c.getEscapedName()+"<>?"), ClsQVariant.fromValue(pValue) ));
			}
		} else {
			if(c.isNullable()) {
				IfBlock ifNull = _ifNot(pValue.callMethod(Optional.has_value));
				ifNull.thenBlock()._return( _this().callMethod(ClsAbstractEntityQuery.where, QString.fromStringConstant(c.getEscapedName()+" is not null"))) ;
				ifNull.elseBlock()._return( _this().callMethod(ClsAbstractEntityQuery.where, QString.fromStringConstant(c.getEscapedName()+"<>?"), ClsQVariant.fromValue( pValue.callMethod(Optional.value) ) ));
			} else {
				_return( _this().callMethod(ClsAbstractEntityQuery.where, QString.fromStringConstant( c.getEscapedName()+"<>?"), ClsQVariant.fromValue(pValue) ));
			}
		}
		
		
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
