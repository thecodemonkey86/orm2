package php.beanrepository.query.method;

import database.column.Column;
import php.bean.BeanCls;
import php.beanrepository.query.ClsBeanQuery;
import php.core.Param;
import php.core.Type;
import php.core.Types;
import php.core.expression.Expression;
import php.core.expression.InlineIfExpression;
import php.core.expression.PhpStringLiteral;
import php.core.instruction.IfBlock;
import php.core.method.Method;
import php.lib.ClsBaseBeanQuery;
import php.lib.ClsSqlQuery;

public class MethodBeanQueryWhereNotEquals extends Method {
	BeanCls bean;
	Param pValue ;
	Column c;
	public MethodBeanQueryWhereNotEquals(ClsBeanQuery query, BeanCls bean,Column c) {
		super(Public, query, "where"+c.getUc1stCamelCaseName()+"NotEquals");
		this.bean=bean;
		Type t = BeanCls.getTypeMapper().columnToType(c);
		
		if(t == null) {
			System.out.println(BeanCls.getTypeMapper().columnToType(c));
		}
		
		pValue = addParam(new Param(t, "value"));
		this.c = c;
	}

	@Override
	public void addImplementation() {
		Expression aSqlQuery = _this().accessAttr(ClsBaseBeanQuery.sqlQuery);
		if(c.isNullable()) {
			IfBlock ifNull = _if(pValue.isNull());
			ifNull.thenBlock()._return( _this().callMethod(ClsBaseBeanQuery.where, new InlineIfExpression(aSqlQuery.callMethod(ClsSqlQuery.getMode)._equals(Types.SqlQuery.accessConstant(ClsSqlQuery.MODE_SELECT)),new PhpStringLiteral("e1." + c.getEscapedName()+" is not null"),new PhpStringLiteral(c.getEscapedName()+" is not null"))) );
			ifNull.elseBlock()._return( _this().callMethod(ClsBaseBeanQuery.where, new InlineIfExpression(aSqlQuery.callMethod(ClsSqlQuery.getMode)._equals(Types.SqlQuery.accessConstant(ClsSqlQuery.MODE_SELECT)),new PhpStringLiteral("e1." + c.getEscapedName()+"<>?"),new PhpStringLiteral(c.getEscapedName()+"<>?")), BeanCls.getTypeMapper().getConvertSqlParamExpression(pValue, c) ) );
		} else {
			_return( _this().callMethod(ClsBaseBeanQuery.where, new InlineIfExpression(aSqlQuery.callMethod(ClsSqlQuery.getMode)._equals(Types.SqlQuery.accessConstant(ClsSqlQuery.MODE_SELECT)),new PhpStringLiteral( "e1." + c.getEscapedName()+"<>?"),new PhpStringLiteral(c.getEscapedName()+"<>?")), BeanCls.getTypeMapper().getConvertSqlParamExpression(pValue, c)  ));
		}
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
