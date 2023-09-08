package cpp.entityquery.method;

import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.QString;
import cpp.entityquery.EntityQueryType;
import cpp.lib.ClsAbstractBeanQuery;
import database.column.Column;

public class MethodEntityQueryBoolWhere extends Method{
	Column c;
	EntityQueryType beanQueryType;
	boolean not;
	public MethodEntityQueryBoolWhere(Cls query,EntityQueryType beanQueryType, Column c, boolean not) {
		super(Public, query.toRef(), "where"+(not?"Not":"")+c.getUc1stCamelCaseName());
		this.c = c;
		this.not=not;
		this.beanQueryType = beanQueryType;
	}

	@Override
	public void addImplementation() {
		//new InlineIfExpression(Expressions.not(_this().accessAttr(ClsBeanQuery.selectFields).callMethod(ClsQString.isEmpty)),
		
		if(beanQueryType == EntityQueryType.Select) {
			_return( _this().callMethod(ClsAbstractBeanQuery.where,  QString.fromStringConstant((not?"not ":"") +"e1." +  c.getEscapedName())));
		} else {
			_return( _this().callMethod(ClsAbstractBeanQuery.where, QString.fromStringConstant((not?"not ":"") +c.getEscapedName())));
		}
		
		
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
