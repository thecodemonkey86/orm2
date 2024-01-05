package sunjava.entityrepository.query.method;

import database.column.Column;
import php.core.Param;
import php.core.Type;
import php.core.Types;
import php.core.expression.Expression;
import php.core.expression.InlineIfExpression;
import php.core.expression.PhpStringLiteral;
import php.core.instruction.IfBlock;
import php.core.method.Method;
import php.entity.EntityCls;
import php.entityrepository.query.ClsEntityQuery;
import php.lib.ClsBaseEntityQuery;
import php.lib.ClsSqlQuery;

public class MethodEntityQueryWhereNotEquals extends Method {
	EntityCls entity;
	Param pValue ;
	Column c;
	public MethodEntityQueryWhereNotEquals(ClsEntityQuery query, EntityCls entity,Column c) {
		super(Public, query, "where"+c.getUc1stCamelCaseName()+"NotEquals");
		this.entity=entity;
		Type t = EntityCls.getTypeMapper().columnToType(c);
		
		if(t == null) {
			System.out.println(EntityCls.getTypeMapper().columnToType(c));
		}
		
		pValue = addParam(new Param(t, "value"));
		this.c = c;
	}

	@Override
	public void addImplementation() {
		Expression aSqlQuery = _this().accessAttr(ClsBaseEntityQuery.sqlQuery);
		if(c.isNullable()) {
			IfBlock ifNull = _if(pValue.isNull());
			ifNull.thenBlock()._return( _this().callMethod(ClsBaseEntityQuery.where, new InlineIfExpression(aSqlQuery.callMethod(ClsSqlQuery.getMode)._equals(Types.SqlQuery.accessConstant(ClsSqlQuery.MODE_SELECT)),new PhpStringLiteral("e1." + c.getEscapedName()+" is not null"),new PhpStringLiteral(c.getEscapedName()+" is not null"))) );
			ifNull.elseBlock()._return( _this().callMethod(ClsBaseEntityQuery.where, new InlineIfExpression(aSqlQuery.callMethod(ClsSqlQuery.getMode)._equals(Types.SqlQuery.accessConstant(ClsSqlQuery.MODE_SELECT)),new PhpStringLiteral("e1." + c.getEscapedName()+"<>?"),new PhpStringLiteral(c.getEscapedName()+"<>?")), EntityCls.getTypeMapper().getConvertSqlParamExpression(pValue, c) ) );
		} else {
			_return( _this().callMethod(ClsBaseEntityQuery.where, new InlineIfExpression(aSqlQuery.callMethod(ClsSqlQuery.getMode)._equals(Types.SqlQuery.accessConstant(ClsSqlQuery.MODE_SELECT)),new PhpStringLiteral( "e1." + c.getEscapedName()+"<>?"),new PhpStringLiteral(c.getEscapedName()+"<>?")), EntityCls.getTypeMapper().getConvertSqlParamExpression(pValue, c)  ));
		}
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
