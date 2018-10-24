package cpp.beanquery.method;

import cpp.bean.BeanCls;
import cpp.beanquery.ClsBeanQuery;
import cpp.core.Method;
import cpp.core.QString;
import cpp.lib.ClsAbstractBeanQuery;
import cpp.lib.ClsQString;
import database.column.Column;
import cpp.core.expression.Expressions;
import cpp.core.expression.InlineIfExpression;

public class MethodBeanQueryWhereIsNull extends Method{
	BeanCls bean;
	Column c;
	public MethodBeanQueryWhereIsNull(ClsBeanQuery query, BeanCls bean,Column c) {
		super(Public, query.toRef(), "where"+c.getUc1stCamelCaseName()+"isNull");
		this.bean=bean;
		this.c = c;
	}

	@Override
	public void addImplementation() {
		_return( _this().callMethod(ClsAbstractBeanQuery.where, new InlineIfExpression(Expressions.not(_this().accessAttr(ClsBeanQuery.selectFields).callMethod(ClsQString.isEmpty)),QString.fromStringConstant("b1." + c.getEscapedName()+" is null"),QString.fromStringConstant(c.getEscapedName()+" is null"))));
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
