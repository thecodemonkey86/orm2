package php.beanrepository.query;

import database.column.Column;
import php.bean.EntityCls;
import php.beanrepository.query.method.ConstructorBeanQuery;
import php.beanrepository.query.method.MethodAddRelatedTableJoins;
import php.beanrepository.query.method.MethodBeanQueryFetch;
import php.beanrepository.query.method.MethodBeanQueryFetchOne;
import php.beanrepository.query.method.MethodBeanQueryWhereEquals;
import php.beanrepository.query.method.MethodBeanQueryWhereIn;
import php.beanrepository.query.method.MethodBeanQueryWhereIsNull;
import php.beanrepository.query.method.MethodBeanQueryWhereNotEquals;
import php.beanrepository.query.method.MethodBeanQueryWhereNotIn;
import php.beanrepository.query.method.MethodGetAllSelectFields;
import php.beanrepository.query.method.MethodGetTableName;
import php.beanrepository.query.method.MethodLimitAndOffset;
import php.core.PhpCls;
import php.lib.ClsBaseEntityQuery;

public class ClsBeanQuery extends PhpCls {

	protected static String beanQueryNamespace;
	
	public static void setBeanQueryNamespace(String beanQueryNamespace) {
		ClsBeanQuery.beanQueryNamespace = beanQueryNamespace;
	}
	
	public ClsBeanQuery(EntityCls cls) {
		super(cls.getName()+ "EntityQuery",beanQueryNamespace);
		
		setSuperclass(new ClsBaseEntityQuery(cls));
		setConstructor(new ConstructorBeanQuery());
		addMethod(new MethodBeanQueryFetch(cls));
		addMethod(new MethodBeanQueryFetchOne(cls));
		addMethod(new MethodAddRelatedTableJoins(cls));
		addMethod(new MethodGetAllSelectFields(cls));
		addMethod(new MethodGetTableName(cls));
		addMethod(new MethodLimitAndOffset(cls,this));
		getMethod(ClsBaseEntityQuery.select).setReturnType(this);
		getMethod(ClsBaseEntityQuery.where).setReturnType(this);
		for(Column c : cls.getTbl().getAllColumns()) {
			addMethod(new MethodBeanQueryWhereEquals(this, cls, c));
			addMethod(new MethodBeanQueryWhereIn(this, cls, c));
			addMethod(new MethodBeanQueryWhereNotEquals(this, cls, c));
			addMethod(new MethodBeanQueryWhereNotIn(this, cls, c));
			
			if(c.isNullable()) {
				addMethod(new MethodBeanQueryWhereIsNull(this, cls, c));
			}
		}
	}


}
