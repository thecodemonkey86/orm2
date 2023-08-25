package sunjava.entityrepository.query;

import sunjava.core.JavaCls;
import sunjava.entity.EntityCls;
import sunjava.entityrepository.query.method.ConstructorBeanQuery;
import sunjava.entityrepository.query.method.MethodAddRelatedTableJoins;
import sunjava.entityrepository.query.method.MethodBeanQueryFetch;
import sunjava.entityrepository.query.method.MethodBeanQueryFetchOne;
import sunjava.entityrepository.query.method.MethodGetAllSelectFields;
import sunjava.entityrepository.query.method.MethodGetTableName;
import sunjava.lib.ClsBaseEntityQuery;

public class ClsEntityQuery extends JavaCls {

	protected static String beanQueryPackage;
	
	public static void setBeanQueryPackage(String beanQueryPackage) {
		ClsEntityQuery.beanQueryPackage = beanQueryPackage;
	}
	
	public ClsEntityQuery(EntityCls cls) {
		super(cls.getName()+ "EntityQuery",beanQueryPackage);
		addImport((JavaCls) EntityCls.getTypeMapper().getSqlQueryClass());
		setSuperclass(new ClsBaseEntityQuery(cls));
		addConstructor(new ConstructorBeanQuery());
		addMethod(new MethodBeanQueryFetch(cls));
		addMethod(new MethodBeanQueryFetchOne(cls));
		addMethod(new MethodAddRelatedTableJoins(cls));
		addMethod(new MethodGetAllSelectFields(cls));
		addMethod(new MethodGetTableName(cls));
	}


}
