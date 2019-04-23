package cpp.beanquery.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.beanquery.ClsBeanQuerySelect;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.Type;
import database.column.Column;


public class MethodBeanQueryWhereIn extends Method {
	BeanCls bean;
	Param pValue ;
	Column c;
	public MethodBeanQueryWhereIn(ClsBeanQuerySelect query, BeanCls bean,Column c) {
		super(Public, query, "where"+c.getUc1stCamelCaseName()+"In");
		this.bean=bean;
		Type t = BeanCls.getDatabaseMapper().columnToType(c);
		
		if(t == null) {
			System.out.println(BeanCls.getDatabaseMapper().columnToType(c));
		}
		
		pValue = addParam(new Param(Types.qvector(t), "value"));
		this.c = c;
	}

	@Override
	public void addImplementation() {
		throw new RuntimeException("not impl");
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
