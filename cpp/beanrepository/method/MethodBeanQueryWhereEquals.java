package cpp.beanrepository.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.beanrepository.ClsBeanQuery;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.StaticCast;
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
//		addInstr(new Instruction() {
//			@Override
//			public String toString() {
//				return "qDebug()<<qu->toString();";
//			}
//		});
		_return(new StaticCast(returnType, _this().callMethod(ClsAbstractBeanQuery.where, QString.fromStringConstant( c.getEscapedName()+"=?"), Types.QVariant.callStaticMethod(ClsQVariant.fromValue, pValue) )));
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
