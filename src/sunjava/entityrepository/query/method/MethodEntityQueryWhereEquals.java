package sunjava.entityrepository.query.method;

import database.column.Column;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Type;
import sunjava.core.expression.Expression;
import sunjava.core.expression.InlineIfExpression;
import sunjava.core.expression.JavaStringLiteral;
import sunjava.core.instruction.IfBlock;
import sunjava.entity.EntityCls;
import sunjava.entityrepository.query.ClsEntityQuery;
import sunjava.lib.ClsBaseEntityQuery;
import sunjava.lib.ClsSqlQuery;
 

public class MethodEntityQueryWhereEquals extends Method {
	EntityCls bean;
	Param pValue ;
	Column c;
	public MethodEntityQueryWhereEquals(ClsEntityQuery query, EntityCls bean,Column c) {
		super(Public, query, getMethodName(c));
		this.bean=bean;
		Type t = EntityCls.getTypeMapper().columnToType(c);
		
		if(t == null) {
			System.out.println(EntityCls.getTypeMapper().columnToType(c));
		}
		
		pValue = addParam(new Param(t, "value"));
		this.c = c;
	}

	public static String getMethodName(Column c) {
		return "where"+c.getUc1stCamelCaseName()+"Equals";
	}

	@Override
	public void addImplementation() {
//		Expression aSqlQuery = _this().accessAttr(ClsBaseEntityQuery.sqlQuery);
//		if(c.isNullable()) {
//			IfBlock ifNull = _if(pValue.isNull());
//			ifNull.thenBlock()._return( _this().callMethod(ClsBaseEntityQuery.where, new InlineIfExpression(aSqlQuery.callMethod(ClsSqlQuery.getMode)._equals(Types.SqlQuery.accessConstant(ClsSqlQuery.MODE_SELECT)), new JavaStringLiteral("e1." + c.getEscapedName()+" is null"), new JavaStringLiteral(c.getEscapedName()+" is null")) ) );
//			ifNull.elseBlock()._return( _this().callMethod(ClsBaseEntityQuery.where, new InlineIfExpression(aSqlQuery.callMethod(ClsSqlQuery.getMode)._equals(Types.SqlQuery.accessConstant(ClsSqlQuery.MODE_SELECT)), new JavaStringLiteral("e1." + c.getEscapedName()+"=?"),new JavaStringLiteral(c.getEscapedName()+"=?")), pValue ) );
//		} else {
//			_return( _this().callMethod(ClsBaseEntityQuery.where, new InlineIfExpression(aSqlQuery.callMethod(ClsSqlQuery.getMode)._equals(Types.SqlQuery.accessConstant(ClsSqlQuery.MODE_SELECT)),new JavaStringLiteral( "e1." + c.getEscapedName()+"=?"),new JavaStringLiteral(c.getEscapedName()+"=?")), pValue  ));
//		}
//		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
