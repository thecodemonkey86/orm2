package cpp.beanquery.method;

import cpp.bean.BeanCls;
import cpp.beanquery.BeanQueryType;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.QString;
import cpp.lib.ClsAbstractBeanQuery;
import database.column.Column;

public class MethodBeanQueryWhereIsNull extends Method{
	BeanCls bean;
	BeanQueryType beanQueryType;
	Column c;
	public MethodBeanQueryWhereIsNull(Cls query,BeanQueryType beanQueryType, BeanCls bean,Column c) {
		super(Public, query.toRef(), "where"+c.getUc1stCamelCaseName()+"isNull");
		this.bean=bean;
		this.c = c;
		this.beanQueryType = beanQueryType;
	}

	@Override
	public void addImplementation() {
		if(beanQueryType == BeanQueryType.Select) {
			_return( _this().callMethod(ClsAbstractBeanQuery.where,QString.fromStringConstant("b1." + c.getEscapedName()+" is null")));
		} else {
			_return( _this().callMethod(ClsAbstractBeanQuery.where,QString.fromStringConstant(c.getEscapedName()+" is null")));
		}
		
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
