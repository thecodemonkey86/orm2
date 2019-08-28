package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.Type;
import cpp.entity.EntityCls;
import cpp.entityquery.ClsEntityQuerySelect;
import database.column.Column;


public class MethodEntityQueryWhereIn extends Method {
	EntityCls bean;
	Param pValue ;
	Column c;
	public MethodEntityQueryWhereIn(ClsEntityQuerySelect query, EntityCls bean,Column c) {
		super(Public, query, "where"+c.getUc1stCamelCaseName()+"In");
		this.bean=bean;
		Type t = EntityCls.getDatabaseMapper().columnToType(c);
		
		if(t == null) {
			System.out.println(EntityCls.getDatabaseMapper().columnToType(c));
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
