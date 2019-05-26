package cpp.beanquery.method;

import cpp.bean.BeanCls;
import cpp.beanquery.BeanQueryType;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.QString;
import cpp.lib.ClsAbstractBeanQuery;
import database.column.Column;

public class MethodBeanQueryWhereIsNotNull extends Method{
	BeanCls bean;
	BeanQueryType beanQueryType;
	Column c;
	public MethodBeanQueryWhereIsNotNull(Cls query,BeanQueryType beanQueryType, BeanCls bean,Column c) {
		super(Public, query.toRef(), "where"+c.getUc1stCamelCaseName()+"IsNotNull");
		this.bean=bean;
		this.c = c;
		this.beanQueryType = beanQueryType;
	}

	@Override
	public void addImplementation() {
		if(beanQueryType == BeanQueryType.Select) {
			_return( _this().callMethod(ClsAbstractBeanQuery.where,QString.fromStringConstant("b1." + c.getEscapedName()+" is not null")));
		} else {
			_return( _this().callMethod(ClsAbstractBeanQuery.where,QString.fromStringConstant(c.getEscapedName()+" is not null")));
		}
		
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
