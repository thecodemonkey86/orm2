package sunjava.entityrepository.query;

import sunjava.core.JavaCls;
import sunjava.entity.EntityCls;
import sunjava.entityrepository.query.method.ConstructorEntityQuery;
import sunjava.entityrepository.query.method.MethodAddRelatedTableJoins;
import sunjava.entityrepository.query.method.MethodEntityQueryFetch;
import sunjava.entityrepository.query.method.MethodEntityQueryFetchOne;
import sunjava.entityrepository.query.method.MethodGetAllSelectFields;
import sunjava.entityrepository.query.method.MethodGetTableName;
import sunjava.lib.ClsBaseEntityQuery;

public class ClsEntityQuery extends JavaCls {

	protected static String entityQueryPackage;
	
	public static void setEntityQueryPackage(String entityQueryPackage) {
		ClsEntityQuery.entityQueryPackage = entityQueryPackage;
	}
	
	public ClsEntityQuery(EntityCls cls) {
		super(cls.getName()+ "EntityQuery",entityQueryPackage);
		addImport((JavaCls) EntityCls.getTypeMapper().getSqlQueryClass());
		setSuperclass(new ClsBaseEntityQuery(cls));
		addConstructor(new ConstructorEntityQuery());
		addMethod(new MethodEntityQueryFetch(cls));
		addMethod(new MethodEntityQueryFetchOne(cls));
		addMethod(new MethodAddRelatedTableJoins(cls));
		addMethod(new MethodGetAllSelectFields(cls));
		addMethod(new MethodGetTableName(cls));
	}


}
