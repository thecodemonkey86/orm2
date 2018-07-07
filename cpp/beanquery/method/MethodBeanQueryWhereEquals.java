package cpp.beanquery.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.beanquery.ClsBeanQuery;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.lib.ClsAbstractBeanQuery;
import cpp.lib.ClsQVariant;
import database.column.Column;

public class MethodBeanQueryWhereEquals extends Method{
	BeanCls bean;
	Param pValue ;
	Column c;
	public MethodBeanQueryWhereEquals(ClsBeanQuery query, BeanCls bean,Column c) {
		super(Public, query.toRef(), "where"+c.getUc1stCamelCaseName()+"Equals");
		this.bean=bean;
		pValue = addParam(new Param(BeanCls.getDatabaseMapper().columnToType(c), "value"));
		this.c = c;
	}

	@Override
	public void addImplementation() {
		_return( _this().callMethod(ClsAbstractBeanQuery.where, QString.fromStringConstant("b1."+ c.getEscapedName()+"=?"), Types.QVariant.callStaticMethod(ClsQVariant.fromValue, pValue) ));
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
