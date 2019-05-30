package cpp.entityquery.method;

import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.QString;
import cpp.entity.EntityCls;
import cpp.entityquery.EntityQueryType;
import cpp.lib.ClsAbstractBeanQuery;
import database.column.Column;

public class MethodEntityQueryWhereIsNotNull extends Method{
	EntityCls bean;
	EntityQueryType beanQueryType;
	Column c;
	public MethodEntityQueryWhereIsNotNull(Cls query,EntityQueryType beanQueryType, EntityCls bean,Column c) {
		super(Public, query.toRef(), "where"+c.getUc1stCamelCaseName()+"IsNotNull");
		this.bean=bean;
		this.c = c;
		this.beanQueryType = beanQueryType;
	}

	@Override
	public void addImplementation() {
		if(beanQueryType == EntityQueryType.Select) {
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
