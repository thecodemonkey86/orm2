package php.beanrepository.query.method;

import database.column.Column;
import php.bean.BeanCls;
import php.beanrepository.query.ClsBeanQuery;
import php.core.Type;
import php.core.Types;
import php.core.expression.Expression;
import php.core.expression.InlineIfExpression;
import php.core.expression.PhpStringLiteral;
import php.core.method.Method;
import php.lib.ClsBaseBeanQuery;
import php.lib.ClsSqlQuery;

public class MethodBeanQueryWhereIsNull extends Method {
	BeanCls bean;
	Column c;
	public MethodBeanQueryWhereIsNull(ClsBeanQuery query, BeanCls bean,Column c) {
		super(Public, query, "where"+c.getUc1stCamelCaseName()+"IsNull");
		this.bean=bean;
		Type t = BeanCls.getTypeMapper().columnToType(c);
		
		if(t == null) {
			System.out.println(BeanCls.getTypeMapper().columnToType(c));
		}
		
		this.c = c;
	}

	@Override
	public void addImplementation() {
		Expression aSqlQuery = _this().accessAttr(ClsBaseBeanQuery.sqlQuery);
		_return( _this().callMethod(ClsBaseBeanQuery.where, new InlineIfExpression(aSqlQuery.callMethod(ClsSqlQuery.getMode)._equals(Types.SqlQuery.accessConstant(ClsSqlQuery.MODE_SELECT)), new PhpStringLiteral("e1." + c.getEscapedName()+" is null"), new PhpStringLiteral(c.getEscapedName()+" is null")) ) );
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
