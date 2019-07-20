package php.beanrepository.query.method;

import database.column.Column;
import php.bean.BeanCls;
import php.beanrepository.query.ClsBeanQuery;
import php.core.Param;
import php.core.PhpFunctions;
import php.core.Type;
import php.core.Types;
import php.core.expression.Expression;
import php.core.expression.InlineIfExpression;
import php.core.expression.ParenthesesExpression;
import php.core.expression.PhpStringLiteral;
import php.core.method.Method;
import php.lib.ClsBaseBeanQuery;
import php.lib.ClsSqlQuery;
import php.lib.ClsSqlUtil;

public class MethodBeanQueryWhereNotIn extends Method {
	BeanCls bean;
	Param pValue ;
	Column c;
	public MethodBeanQueryWhereNotIn(ClsBeanQuery query, BeanCls bean,Column c) {
		super(Public, query, "where"+c.getUc1stCamelCaseName()+"NotIn");
		this.bean=bean;
		Type t = BeanCls.getTypeMapper().columnToType(c);
		
		if(t == null) {
			System.out.println(BeanCls.getTypeMapper().columnToType(c));
		}
		
		pValue = addParam(new Param(Types.array(t), "value"));
		this.c = c;
	}

	@Override
	public void addImplementation() {
		Expression aSqlQuery = _this().accessAttr(ClsBaseBeanQuery.sqlQuery);
		_return( _this().callMethod(ClsBaseBeanQuery.where, new ParenthesesExpression( new InlineIfExpression(aSqlQuery.callMethod(ClsSqlQuery.getMode)._equals(Types.SqlQuery.accessConstant(ClsSqlQuery.MODE_SELECT)),new PhpStringLiteral( "e1." + c.getEscapedName()+" not in "),new PhpStringLiteral(c.getEscapedName()+" not in "))).concat(Types.SqlUtil.callStaticMethod(ClsSqlUtil.getPlaceholders, PhpFunctions.count.call(pValue))),  pValue ));
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
